
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.AuditRecord;
import acme.entities.CodeAudit;
import acme.entities.Project;
import acme.enumerated.Mark;
import acme.enumerated.Type;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditPublishService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
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
		CodeAudit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		super.bind(object, "code", "execution", "type", "correctiveActions", "optionalLink");
	}

	//CAMBIOS
	@Override
	public void validate(final CodeAudit object) {
		assert object != null;
		Collection<AuditRecord> auditRecords;
		int id;

		id = super.getRequest().getData("id", int.class);
		auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(id);

		super.state(!auditRecords.isEmpty(), "*", "auditor.code-audit.form.error.isEmpty");
		super.state(object.getMark(auditRecords) != Mark.F && object.getMark(auditRecords) != Mark.F_MINUS, "*", "auditor.code-audit.form.error.markNotAtLeastC");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit duplicated;

			duplicated = this.repository.findOneCodeAuditByCode(object.getCode());
			super.state(duplicated == null || duplicated.equals(object), "code", "auditor.code-audit.form.error.duplicated");
		}
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {

		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choices;
		Collection<AuditRecord> auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(object.getId());
		SelectChoices choicesType;

		projects = this.repository.findManyProjectsAvailable();

		choicesType = SelectChoices.from(Type.class, object.getType());
		choices = SelectChoices.from(projects, "title", object.getProject());

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "optionalLink", "draftMode");
		dataset.put("type", choicesType);
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("mark", object.getMark(auditRecords));

		super.getResponse().addData(dataset);
	}

}
