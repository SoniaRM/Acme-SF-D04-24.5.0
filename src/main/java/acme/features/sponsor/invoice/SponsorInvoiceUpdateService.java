
package acme.features.sponsor.invoice;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.Invoice;
import acme.entities.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceUpdateService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int invoiceId;
		Invoice invoice;
		Sponsor sponsor;

		invoiceId = super.getRequest().getData("id", int.class);

		invoice = this.repository.findOneInvoiceById(invoiceId);
		sponsor = invoice == null ? null : invoice.getSponsor();
		status = invoice != null && invoice.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsor);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneInvoiceById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Invoice object) {
		assert object != null;

		super.bind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link");
	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.repository.existsOtherByCodeAndId(object.getCode(), object.getId()), "code", "sponsor.invoice.form.error.duplicated");

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			Date maximumDeadline;

			if (!super.getBuffer().getErrors().hasErrors("registrationTime")) {
				maximumDeadline = MomentHelper.deltaFromMoment(object.getRegistrationTime(), 1, ChronoUnit.MONTHS);
				super.state(MomentHelper.isAfter(object.getDueDate(), maximumDeadline), "dueDate", "sponsor.invoice.form.error.to-close-from-registration");

			} else
				super.state(false, "dueDate", "sponsor.invoice.form.error.incorrect-registration-time");
		}

		if (!super.getBuffer().getErrors().hasErrors("quantity")) {
			super.state(object.getQuantity().getAmount() > 0, "quantity", "sponsor.invoice.form.error.negative-salary");
			super.state(Arrays.asList(this.repository.findAcceptedCurrencies().split(",")).contains(object.getQuantity().getCurrency()), "quantity", "sponsor.invoice.form.error.invalid-currency");
		}
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;
		Sponsorship sponsorship;
		Double invoicesAmounts;
		Money finalMoney;
		String systemCurrency;

		this.repository.save(object);

		sponsorship = object.getSponsorship();

		invoicesAmounts = this.repository.findManyInvoicesBySponsorshipId(sponsorship.getId()).stream() //
			.mapToDouble(i -> i.totalAmount().getAmount() / this.repository.findMoneyConvertByMoneyCurrency(i.totalAmount().getCurrency())) //
			.sum();

		systemCurrency = this.repository.findSystemConfiguration().getSystemCurrency();

		finalMoney = new Money();
		finalMoney.setAmount(Math.round(invoicesAmounts * this.repository.findMoneyConvertByMoneyCurrency(systemCurrency) * 100.0) / 100.0);
		finalMoney.setCurrency(this.repository.findSystemConfiguration().getSystemCurrency());

		sponsorship.setAmount(finalMoney);
		this.repository.save(sponsorship);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
