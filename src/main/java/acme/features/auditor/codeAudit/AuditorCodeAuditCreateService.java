
package acme.features.auditor.codeAudit;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.CodeAudit;
import acme.entities.Project;
import acme.enumerated.Type;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditCreateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		/*
		 * final boolean status;
		 * status = super.getRequest().getPrincipal().hasRole(Auditor.class);
		 * super.getResponse().setAuthorised(status);
		 */
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CodeAudit object;
		Auditor auditor;

		auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new CodeAudit();
		object.setDraftMode(true);
		object.setAuditor(auditor);
		//object.setExecution(MomentHelper.getCurrentMoment());  //Comprobar

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		//super.bind(object, "code", "execution", "type", "correctiveActions", "optionalLink", "project");

		int projectId;
		Project project;
		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOnePublishedProjectById(projectId);
		super.bind(object, "code", "execution", "type", "correctiveActions", "mark", "optionalLink");
		object.setProject(project);
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;
		Date lowerLimit;

		lowerLimit = new Date(946681200000L); // 2000/01/01 00:00:00

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit existing;
			existing = this.repository.findOneCodeAuditByCode(object.getCode());
			super.state(existing == null, "code", "auditor.code-audit.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("execution")) {
			Date execution;
			execution = object.getExecution();

			if (execution != null)
				super.state(MomentHelper.isAfterOrEqual(execution, lowerLimit), "execution", "auditor.code-audit.form.error.date-lower-limit");

		}

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;
		//object.setDraftMode(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Collection<Project> projects;
		SelectChoices choicesType;
		SelectChoices choices;
		Dataset dataset;

		projects = this.repository.findManyProjectsAvailable();

		choices = SelectChoices.from(projects, "code", object.getProject());
		choicesType = SelectChoices.from(Type.class, object.getType());

		dataset = super.unbind(object, "code", "execution", "correctiveActions", "optionalLink", "draftMode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("type", choicesType.getSelected().getKey());
		dataset.put("types", choicesType);

		super.getResponse().addData(dataset);

	}

}
