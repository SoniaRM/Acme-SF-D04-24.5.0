
package acme.features.sponsor.invoice;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		Sponsorship sponsorship;
		Invoice invoice;

		invoiceId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(invoiceId);
		sponsorship = this.repository.findOneSponsorshipByInvoiceId(invoiceId);
		status = sponsorship != null && sponsorship.isDraftMode() && invoice.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor());

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
		Date lowerLimit;
		Date upperLimit;
		lowerLimit = new Date(946681200000L); // 2000/01/01 00:00:00
		upperLimit = new Date(7289650799000L); // 2200/12/31 23:59:59

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Invoice existing;
			existing = this.repository.findOneInvoiceByCode(object.getCode());
			if (existing == null || existing.getId() != object.getId())
				super.state(existing == null, "code", "sponsor.invoice.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("registrationTime")) {
			Date registrationTime;
			registrationTime = object.getRegistrationTime();

			if (registrationTime != null)
				super.state(MomentHelper.isAfterOrEqual(registrationTime, lowerLimit), "registrationTime", "sponsor.invoice.form.error.date-lower-limit");
		}

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			Date minimumDeadline;
			Date registrationTime;
			registrationTime = object.getRegistrationTime();

			if (registrationTime == null)
				super.state(false, "dueDate", "sponsor.invoice.form.error.too-short");
			else {
				minimumDeadline = MomentHelper.deltaFromMoment(registrationTime, 30, ChronoUnit.DAYS);
				super.state(MomentHelper.isBeforeOrEqual(object.getDueDate(), upperLimit), "dueDate", "sponsor.invoice.form.error.date-upper-limit");
				super.state(MomentHelper.isAfter(object.getDueDate(), minimumDeadline), "dueDate", "sponsor.invoice.form.error.too-short");
			}
		}

		if (!super.getBuffer().getErrors().hasErrors("quantity")) {
			int invoiceId;
			Sponsorship sponsorship;
			invoiceId = super.getRequest().getData("id", int.class);
			sponsorship = this.repository.findOneSponsorshipByInvoiceId(invoiceId);

			super.state(sponsorship.getAmount().getCurrency().equals(object.getQuantity().getCurrency()), "quantity", "sponsor.invoice.form.error.different-currency");
			super.state(object.getQuantity().getAmount() > 0, "quantity", "sponsor.invoice.form.error.negative-amount");
			super.state(object.getQuantity().getAmount() <= 1000000, "quantity", "sponsor.invoice.form.error.quantity-upper-limit");
		}
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link");
		dataset.put("masterId", object.getSponsorship().getId());
		dataset.put("draftMode", object.isDraftMode());
		dataset.put("totalAmount", object.totalAmount());
		dataset.put("sponsorship", object.getSponsorship().getCode());

		super.getResponse().addData(dataset);
	}

}
