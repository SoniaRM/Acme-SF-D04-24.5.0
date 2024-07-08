
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
import acme.enumerated.Type;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditDeleteService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorCodeAuditRepository repository;

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

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		Collection<AuditRecord> auditRecords;

		auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(object.getId());
		this.repository.deleteAll(auditRecords);
		this.repository.delete(object);
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
