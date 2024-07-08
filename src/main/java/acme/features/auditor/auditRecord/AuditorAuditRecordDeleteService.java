
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
public class AuditorAuditRecordDeleteService extends AbstractService<Auditor, AuditRecord> {

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
		status = super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor()) || object != null && !object.isDraftMode();

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
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink");
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final AuditRecord object) {

		assert object != null;

		Dataset dataset;
		SelectChoices marks;

		marks = SelectChoices.from(Mark.class, object.getMark());

		dataset = super.unbind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink");
		dataset.put("codeAuditId", object.getCodeAudit().getId());
		dataset.put("draftMode", object.isDraftMode());
		dataset.put("codeAudit", object.getCodeAudit().getCode());
		dataset.put("mark", marks.getSelected().getKey());
		dataset.put("marks", marks);

		super.getResponse().addData(dataset);
		/*
		 * assert object != null;
		 * 
		 * Dataset dataset;
		 * Collection<CodeAudit> codeAudits;
		 * SelectChoices choices;
		 * SelectChoices marks;
		 * 
		 * marks = SelectChoices.from(Mark.class, object.getMark());
		 * codeAudits = this.repository.findAllCodeAudits();
		 * choices = SelectChoices.from(codeAudits, "code", object.getCodeAudit());
		 * 
		 * dataset = super.unbind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink");
		 * dataset.put("codeAudit", choices.getSelected().getKey());
		 * dataset.put("codeAudits", choices);
		 * dataset.put("mark", marks.getSelected().getKey());
		 * dataset.put("marks", marks);
		 * 
		 * super.getResponse().addData(dataset);
		 */

	}
}
