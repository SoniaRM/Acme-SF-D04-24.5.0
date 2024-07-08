
package acme.features.auditor.codeAudit;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.AuditRecord;
import acme.entities.CodeAudit;
import acme.entities.Project;
import acme.enumerated.Type;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditUpdateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status;
		int codeAuditId;
		CodeAudit object;
		Auditor auditor;

		codeAuditId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(codeAuditId);
		auditor = object == null ? null : object.getAuditor();
		status = object != null && object.isDraftMode() && super.getRequest().getPrincipal().hasRole(auditor);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit codeAudit;
		int id;

		id = this.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(codeAudit);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

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
			CodeAudit ca = this.repository.findOneCodeAuditByCode(object.getCode());
			Boolean repeatedCode = ca == null || object.getId() == ca.getId();
			super.state(repeatedCode, "code", "auditor.code-audit.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("execution")) {
			Date execution = object.getExecution();

			if (execution != null)
				super.state(MomentHelper.isAfterOrEqual(execution, lowerLimit), "execution", "auditor.code-audit.form.error.date-lower-limit");

		}
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;
		object.setDraftMode(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choices;
		SelectChoices choicesType;

		Collection<AuditRecord> auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(object.getId());

		projects = this.repository.findManyProjectsAvailable();
		choicesType = SelectChoices.from(Type.class, object.getType());
		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "execution", "correctiveActions", "optionalLink", "draftMode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("type", choicesType.getSelected().getKey());
		dataset.put("types", choicesType);
		dataset.put("mark", object.getMark(auditRecords));

		super.getResponse().addData(dataset);
	}

}
