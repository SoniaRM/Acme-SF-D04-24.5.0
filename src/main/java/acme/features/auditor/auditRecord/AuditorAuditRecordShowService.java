
package acme.features.auditor.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.AuditRecord;
import acme.entities.CodeAudit;
import acme.enumerated.Mark;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordShowService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int auditRecordId;
		AuditRecord object;
		CodeAudit codeAudit;

		auditRecordId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(auditRecordId);
		codeAudit = object == null ? null : object.getCodeAudit();
		status = super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor()) && object != null;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(Mark.class, object.getMark());

		dataset = super.unbind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink");
		dataset.put("mark", choices.getSelected().getKey());
		dataset.put("marks", choices);
		dataset.put("codeAuditId", object.getCodeAudit().getId());
		dataset.put("draftMode", object.isDraftMode());
		dataset.put("codeAudit", object.getCodeAudit().getCode());

		super.getResponse().addData(dataset);
	}

}
