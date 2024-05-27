
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Invoice;
import acme.entities.Project;
import acme.entities.Sponsorship;
import acme.enumerated.ProjectType;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Sponsorship sponsorship;
		Sponsor sponsor;

		masterId = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findOneSponsorshipById(masterId);
		sponsor = sponsorship == null ? null : sponsorship.getSponsor();
		status = sponsorship != null && sponsorship.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsor);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSponsorshipById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;
		int projectId;
		Project project;
		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);
		super.bind(object, "code", "moment", "durationStart", "durationEnd", "amount", "projectType", "email", "link");
		object.setProject(project);
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;
		Date lowerLimit;
		Date upperLimit;
		Date upperLimitMonthBefore;
		lowerLimit = new Date(946681200000L); // 2000/01/01 00:00:00
		upperLimit = new Date(7289650799000L); // 2200/12/31 23:59:59
		upperLimitMonthBefore = new Date(7287058739000L); // 2200/12/01 23:58:59

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;
			existing = this.repository.findOneSponsorshipByCode(object.getCode());
			if (existing == null || existing.getId() != object.getId())
				super.state(existing == null, "code", "sponsor.sponsorship.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("moment")) {
			Date moment;
			moment = object.getMoment();

			if (moment != null)
				super.state(MomentHelper.isAfterOrEqual(moment, lowerLimit), "moment", "sponsor.sponsorship.form.error.date-lower-limit");
		}

		if (!super.getBuffer().getErrors().hasErrors("durationStart")) {
			Date moment;
			Date durationStart;
			moment = object.getMoment();
			durationStart = object.getDurationStart();

			if (moment == null)
				super.state(false, "durationStart", "sponsor.sponsorship.form.error.after-moment");
			else {
				super.state(MomentHelper.isBeforeOrEqual(durationStart, upperLimitMonthBefore), "durationStart", "sponsor.sponsorship.form.error.date-upper-limit-month-before");
				super.state(MomentHelper.isAfter(durationStart, moment), "durationStart", "sponsor.sponsorship.form.error.after-moment");
			}
		}

		if (!super.getBuffer().getErrors().hasErrors("durationEnd")) {
			Date minimumDeadline;
			Date durationStart;
			Date durationEnd;
			durationStart = object.getDurationStart();
			durationEnd = object.getDurationEnd();

			if (object.getDurationStart() == null)
				super.state(false, "durationEnd", "sponsor.sponsorship.form.error.too-short");
			else {
				minimumDeadline = MomentHelper.deltaFromMoment(durationStart, 30, ChronoUnit.DAYS);
				super.state(MomentHelper.isBeforeOrEqual(durationEnd, upperLimit), "durationEnd", "sponsor.sponsorship.form.error.date-upper-limit");
				super.state(MomentHelper.isAfter(durationEnd, minimumDeadline), "durationEnd", "sponsor.sponsorship.form.error.too-short");
			}
		}

		Collection<Invoice> invoices = this.repository.findManyInvoicesBySponsorshipId(object.getId());
		double sumTotal = 0.0;
		boolean invoicesPublished = true;
		if (object.getAmount() != null) {
			String currency = object.getAmount().getCurrency();
			for (Invoice i : invoices) {
				invoicesPublished = invoicesPublished && !i.isDraftMode();
				if (i.getQuantity().getCurrency().equals(currency))
					sumTotal += i.totalAmount();
			}

			// Trunco sumTotal a 2 decimales
			double factor = Math.pow(10, 2);
			sumTotal = Math.round(sumTotal * factor) / factor;
		} else
			sumTotal = -1234.56;

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(invoicesPublished, "project", "sponsor.sponrsorship.form.error.invoices-not-published");

		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			Sponsorship sponsorship = this.repository.findOneSponsorshipById(object.getId());
			Money amount = object.getAmount();

			super.state(amount.getCurrency().equals(sponsorship.getAmount().getCurrency()), "amount", "sponsor.sponsorship.form.error.different-currency");
			super.state(amount.getAmount() > 0, "amount", "sponsor.sponsorship.form.error.negative-amount");
			super.state(amount.getAmount() == sumTotal, "amount", "sponsor.sponsorship.form.error.invoices-amount");
			super.state(amount.getCurrency().equals("EUR") || amount.getCurrency().equals("USD") || amount.getCurrency().equals("GBP"), "amount", "sponsor.sponsorship.form.error.wrong-currency");
			super.state(amount.getAmount() <= 1000000, "amount", "sponsor.sponsorship.form.error.amount-upper-limit");
		}
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Collection<Project> allProjects;
		SelectChoices projects;
		SelectChoices choices;
		Dataset dataset;

		allProjects = this.repository.findManyPublishedProjects();
		projects = SelectChoices.from(allProjects, "code", object.getProject());
		choices = SelectChoices.from(ProjectType.class, object.getProjectType());

		dataset = super.unbind(object, "code", "moment", "durationStart", "durationEnd", "amount", "projectType", "email", "link");
		dataset.put("project", projects.getSelected().getKey());
		dataset.put("projects", projects);
		dataset.put("types", choices);
		dataset.put("draftMode", object.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
