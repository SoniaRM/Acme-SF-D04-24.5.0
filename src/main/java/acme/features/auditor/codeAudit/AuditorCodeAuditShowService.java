
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
public class AuditorCodeAuditShowService extends AbstractService<Auditor, CodeAudit> {

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
		status = super.getRequest().getPrincipal().hasRole(auditor) && object != null;

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

		dataset = super.unbind(object, "code", "execution", "type", "correctiveActions", "optionalLink");
		dataset.put("types", choicesType);
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("execution", object.getExecution());
		dataset.put("mark", object.getMark(auditRecords));
		dataset.put("draftMode", object.isDraftMode());

		super.getResponse().addData(dataset);

		int codeAuditId;
		CodeAudit codeAudit;
		final boolean show;
		Auditor auditor;

		codeAuditId = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditById(codeAuditId);
		auditor = object == null ? null : codeAudit.getAuditor();
		show = !codeAudit.isDraftMode() && super.getRequest().getPrincipal().getActiveRoleId() == auditor.getId();

		super.getResponse().addGlobal("show", show);
	}
}
