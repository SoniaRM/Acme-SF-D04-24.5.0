
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Project;
import acme.entities.Sponsorship;
import acme.enumerated.ProjectType;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipCreateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Sponsorship object;
		Sponsor sponsor;
		sponsor = this.repository.findOneSponsorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Sponsorship();
		object.setDraftMode(true);
		object.setSponsor(sponsor);

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

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;
			existing = this.repository.findOneSponsorshipByCode(object.getCode());
			super.state(existing == null, "code", "sponsor.sponsorship.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("durationStart")) {
			Date minimumDeadline;
			minimumDeadline = object.getMoment();
			if (minimumDeadline == null)
				super.state(false, "durationStart", "sponsor.sponsorship.form.error.after-moment");
			else
				super.state(MomentHelper.isAfter(object.getDurationStart(), minimumDeadline), "durationStart", "sponsor.sponsorship.form.error.after-moment");
		}

		if (!super.getBuffer().getErrors().hasErrors("durationEnd")) {
			Date minimumDeadline;
			if (object.getDurationStart() == null)
				super.state(false, "durationEnd", "sponsor.sponsorship.form.error.too-short");
			else {
				minimumDeadline = MomentHelper.deltaFromMoment(object.getDurationStart(), 30, ChronoUnit.DAYS);
				super.state(MomentHelper.isAfter(object.getDurationEnd(), minimumDeadline), "durationEnd", "sponsor.sponsorship.form.error.too-short");
			}
		}

		if (!super.getBuffer().getErrors().hasErrors("amount"))
			super.state(object.getAmount().getAmount() > 0, "amount", "sponsor.sponsorship.form.error.negative-amount");
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;
		object.setDraftMode(true);
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
