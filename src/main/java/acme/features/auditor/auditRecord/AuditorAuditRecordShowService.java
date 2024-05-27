
package acme.features.auditor.auditRecord;

import java.util.Collection;

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

		/*
		 * boolean status = false;
		 * int id;
		 * Auditor auditor;
		 * AuditRecord auditRecord;
		 * 
		 * id = super.getRequest().getData("id", int.class);
		 * auditRecord = this.repository.findOneAuditRecordById(id);
		 * 
		 * auditor = auditRecord == null ? null : auditRecord.getCodeAudit().getAuditor();
		 * status = auditRecord != null && super.getRequest().getPrincipal().hasRole(auditor);
		 * 
		 * super.getResponse().setAuthorised(status);
		 */

		/*
		 * boolean status;
		 * int auditRecordId;
		 * AuditRecord object;
		 * CodeAudit codeAudit;
		 * 
		 * auditRecordId = super.getRequest().getData("id", int.class);
		 * object = this.repository.findOneAuditRecordById(auditRecordId);
		 * codeAudit = object == null ? null : object.getCodeAudit();
		 * status = super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor()) || object != null && !object.isDraftMode();
		 * 
		 * super.getResponse().setAuthorised(status);
		 */
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(object);
		/*
		 * AuditRecord object;
		 * int id;
		 * 
		 * id = super.getRequest().getData("id", int.class);
		 * object = this.repository.findOneAuditRecordById(id);
		 * super.getBuffer().addData(object);
		 */
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(Mark.class, object.getMark());
		Collection<CodeAudit> codeAudits;
		SelectChoices choicesCA;

		codeAudits = this.repository.findAllCodeAudits();
		choicesCA = SelectChoices.from(codeAudits, "code", object.getCodeAudit());

		dataset = super.unbind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink", "draftMode");
		dataset.put("mark", choices.getSelected().getKey());
		dataset.put("marks", choices);
		dataset.put("codeAudit", choicesCA.getSelected().getKey());
		dataset.put("codeAudits", choicesCA);

		super.getResponse().addData(dataset);

		/*
		 * assert object != null;
		 * 
		 * SelectChoices choices;
		 * Dataset dataset;
		 * CodeAudit codeAudit;
		 * 
		 * choices = SelectChoices.from(Mark.class, object.getMark());
		 * codeAudit = object.getCodeAudit();
		 * 
		 * dataset = super.unbind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink", "draftMode");
		 * dataset.put("codeAuditCode", codeAudit.getCode());
		 * dataset.put("mark", choices.getSelected().getKey());
		 * dataset.put("marks", choices);
		 * 
		 * super.getResponse().addData(dataset);
		 */
	}

}
