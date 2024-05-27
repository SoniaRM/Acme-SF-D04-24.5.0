
package acme.features.auditor.auditRecord;

import java.time.temporal.ChronoUnit;
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
import acme.enumerated.Mark;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordCreateService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		final boolean status;
		status = super.getRequest().getPrincipal().hasRole(Auditor.class);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		AuditRecord object;

		object = new AuditRecord();
		object.setDraftMode(true);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink", "codeAudit");
		/*
		 * int id;
		 * CodeAudit codeAudit;
		 * 
		 * id = super.getRequest().getData("codeAuditId", int.class);
		 * codeAudit = this.repository.findOneCodeAuditById(id);
		 * 
		 * super.bind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink");
		 * object.setCodeAudit(codeAudit);
		 */
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord isCodeUnique;
			isCodeUnique = this.repository.findOneAuditRecordByCode(object.getCode());
			super.state(isCodeUnique == null, "code", "auditor.audit-record.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("initialPeriod"))
			if (object.getFinalPeriod() != null && object.getInitialPeriod() != null)
				super.state(MomentHelper.isAfter(object.getFinalPeriod(), object.getInitialPeriod()), "startMoment", "validation.auditrecord.initialIsBefore");
		if (!super.getBuffer().getErrors().hasErrors("finishPeriod"))
			if (object.getFinalPeriod() != null && object.getInitialPeriod() != null) {
				Date end;
				end = MomentHelper.deltaFromMoment(object.getInitialPeriod(), 1, ChronoUnit.HOURS);
				super.state(MomentHelper.isAfterOrEqual(object.getFinalPeriod(), end), "finishMoment", "validation.auditrecord.moment.minimun");
			}
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		this.repository.save(object);
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

		dataset = super.unbind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink", "codeAudit");
		dataset.put("mark", choices.getSelected().getKey());
		dataset.put("marks", choices);
		dataset.put("codeAudit", choicesCA.getSelected().getKey());
		dataset.put("codeAudits", choicesCA);

		super.getResponse().addData(dataset);

		/*
		 * Dataset dataset;
		 * SelectChoices choices;
		 * choices = SelectChoices.from(Mark.class, object.getMark());
		 * 
		 * CodeAudit codeAudit = object.getCodeAudit();
		 * 
		 * dataset = super.unbind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink", "draftMode");
		 * dataset.put("codeAuditCode", codeAudit.getCode());
		 * dataset.put("marks", choices);
		 * dataset.put("codeAuditId", codeAudit.getId());
		 * 
		 * super.getResponse().addData(dataset);
		 */
	}
}
