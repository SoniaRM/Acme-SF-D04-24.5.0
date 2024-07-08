
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
public class AuditorAuditRecordListMineService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int codeAuditId;
		Auditor auditor;
		CodeAudit codeAudit;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(codeAuditId);
		auditor = codeAudit == null ? null : codeAudit.getAuditor();
		status = codeAudit != null && (!codeAudit.isDraftMode() || super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor())) && super.getRequest().getPrincipal().getActiveRoleId() == auditor.getId();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<AuditRecord> objects;
		int codeAuditId;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		objects = this.repository.findManyAuditRecordsByCodeAuditId(codeAuditId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "initialPeriod", "finalPeriod", "mark", "optionalLink", "draftMode", "codeAudit");
		String draftMode = object.isDraftMode() ? "✓" : "x";
		dataset.put("draftMode", draftMode);
		dataset.put("codeAudit", object.getCodeAudit());

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<AuditRecord> objects) {
		assert objects != null;

		int masterId;
		CodeAudit codeAudit;
		boolean showCreate;

		masterId = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.repository.findOneCodeAuditById(masterId);
		showCreate = codeAudit.isDraftMode() && super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor());

		super.getResponse().addGlobal("codeAuditId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
