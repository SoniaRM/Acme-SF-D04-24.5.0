
package acme.features.auditor.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.enumerated.Type;
import acme.forms.AuditorDashboards;
import acme.roles.Auditor;

@Service
public class AuditorDashboardShowService extends AbstractService<Auditor, AuditorDashboards> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		AuditorDashboards dashboard;

		id = super.getRequest().getPrincipal().getActiveRoleId();

		int totalCodeAuditsStatic;
		int totalCodeAuditsDynamic;

		Double avgAuditRecords;
		Double devAuditRecords;
		Double minAuditRecords;
		Double maxAuditRecords;

		Double avgPeriod;
		Double devPeriod;
		Double minPeriod;
		Double maxPeriod;

		totalCodeAuditsStatic = this.repository.totalCodeAuditsType(Type.STATIC, id);
		totalCodeAuditsDynamic = this.repository.totalCodeAuditsType(Type.DYNAMIC, id);

		avgAuditRecords = this.repository.avgNumberOfAuditRecordsPerAudits(id);
		devAuditRecords = this.repository.devNumberOfAuditRecordsPerAudits(id);
		minAuditRecords = this.repository.minNumberOfAuditRecordsPerAudits(id);
		maxAuditRecords = this.repository.maxNumberOfAuditRecordsPerAudits(id);

		avgPeriod = this.repository.avgPeriodOfAuditRecords(id);
		devPeriod = this.repository.devPeriodOfAuditRecords(id);
		minPeriod = this.repository.minPeriodOfAuditRecords(id);
		maxPeriod = this.repository.maxPeriodOfAuditRecords(id);

		dashboard = new AuditorDashboards();
		dashboard.setTotalCodeAuditsStatic(totalCodeAuditsStatic);
		dashboard.setTotalCodeAuditsDynamic(totalCodeAuditsDynamic);
		dashboard.setAvgAuditRecords(avgAuditRecords);
		dashboard.setDevAuditRecords(devAuditRecords);
		dashboard.setMinAuditRecords(minAuditRecords);
		dashboard.setMaxAuditRecords(maxAuditRecords);
		dashboard.setAvgPeriod(avgPeriod);
		dashboard.setDevPeriod(devPeriod);
		dashboard.setMinPeriod(minPeriod);
		dashboard.setMaxPeriod(maxPeriod);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AuditorDashboards object) {
		Dataset dataset;

		dataset = super.unbind(object, "totalCodeAuditsStatic", "totalCodeAuditsDynamic", "avgAuditRecords", "devAuditRecords", "minAuditRecords", "maxAuditRecords", "avgPeriod", "devPeriod", "minPeriod", "maxPeriod");

		super.getResponse().addData(dataset);
	}

}
