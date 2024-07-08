
package acme.features.auditor.auditRecord;

import java.time.temporal.ChronoUnit;

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

		boolean status;
		int masterId;
		CodeAudit codeAudit;

		masterId = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(masterId);
		status = codeAudit != null && codeAudit.isDraftMode() && super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor());

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		AuditRecord object;
		int masterId;
		CodeAudit codeAudit;

		masterId = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(masterId);
		object = new AuditRecord();
		object.setDraftMode(true);
		object.setCodeAudit(codeAudit);

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

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord isCodeUnique;
			isCodeUnique = this.repository.findOneAuditRecordByCode(object.getCode());
			super.state(isCodeUnique == null, "code", "auditor.audit-record.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("initialPeriod")) {
			boolean notNull = object.getCodeAudit().getExecution() != null;
			Boolean timeConcordance = notNull && MomentHelper.isAfter(object.getInitialPeriod(), object.getCodeAudit().getExecution());
			super.state(timeConcordance, "initialPeriod", "auditor.audit-record.form.error.badInitialDate");
		}

		if (!super.getBuffer().getErrors().hasErrors("finalPeriod")) {
			boolean notNull = object.getInitialPeriod() != null;
			Boolean timeConcordance = notNull && MomentHelper.isAfter(object.getFinalPeriod(), object.getInitialPeriod());
			super.state(timeConcordance, "finalPeriod", "auditor.audit-record.form.error.FinalAfterInitial");
		}

		if (!super.getBuffer().getErrors().hasErrors("finalPeriod")) {
			boolean notNull = object.getInitialPeriod() != null;
			Boolean goodDuration = notNull && MomentHelper.isLongEnough(object.getFinalPeriod(), object.getInitialPeriod(), 1, ChronoUnit.HOURS);
			super.state(goodDuration, "finalPeriod", "auditor.auditRecord.form.error.notEnoughDuration");
		}
		/*
		 * if (!super.getBuffer().getErrors().hasErrors("initialPeriod"))
		 * if (object.getFinalPeriod() != null && object.getInitialPeriod() != null) {
		 * super.state(MomentHelper.isAfter(object.getFinalPeriod(), object.getInitialPeriod()), "startMoment", "validation.auditrecord.initialIsBefore");
		 * super.state(MomentHelper.isAfterOrEqual(object.getInitialPeriod(), object.getCodeAudit().getExecution()), "startMoment", "validation.auditrecord.initialIsAfterExecution");
		 * }
		 * if (!super.getBuffer().getErrors().hasErrors("finishPeriod"))
		 * if (object.getFinalPeriod() != null && object.getInitialPeriod() != null) {
		 * Date end;
		 * end = MomentHelper.deltaFromMoment(object.getInitialPeriod(), 1, ChronoUnit.HOURS);
		 * super.state(MomentHelper.isAfterOrEqual(object.getFinalPeriod(), end), "finishMoment", "validation.auditrecord.moment.minimun");
		 * }
		 */
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

		dataset = super.unbind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink", "codeAudit");
		dataset.put("codeAuditId", super.getRequest().getData("codeAuditId", int.class));
		dataset.put("mark", choices.getSelected().getKey());
		dataset.put("marks", choices);
		dataset.put("codeAudit", object.getCodeAudit().getCode());

		super.getResponse().addData(dataset);

	}
}
