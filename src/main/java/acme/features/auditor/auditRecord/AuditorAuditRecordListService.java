
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.AuditRecord;
import acme.entities.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordListService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int codeAuditId;
		CodeAudit codeAudit;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(codeAuditId);

		status = codeAudit != null && (!codeAudit.isDraftMode() || super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<AuditRecord> objects;
		int codeAuditId;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		objects = this.repository.findManyAuditRecordsByCodeAuditId(codeAuditId);

		super.getBuffer().addData(objects);
		super.getResponse().addGlobal("codeAuditId", codeAuditId);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "mark", "draftMode", "finalPeriod");

		super.getResponse().addData(dataset);
	}
}
