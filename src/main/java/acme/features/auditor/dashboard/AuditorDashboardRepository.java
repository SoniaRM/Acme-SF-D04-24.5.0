
package acme.features.auditor.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.enumerated.Type;
import acme.roles.Auditor;

@Repository
public interface AuditorDashboardRepository extends AbstractRepository {

	@Query("select a from Auditor a where a.id = :id")
	Auditor findOneAuditorByUserAccountId(int id);

	@Query("select count(ca) from CodeAudit ca where ca.type = :type AND ca.auditor.id = :id ")
	int totalCodeAuditsType(Type type, int id);

	@Query("select avg(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id) from CodeAudit ca where ca.auditor.id = :id")
	Double avgNumberOfAuditRecordsPerAudits(int id);

	@Query("select stddev((select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id)) from CodeAudit ca where ca.auditor.id = :id")
	Double devNumberOfAuditRecordsPerAudits(int id);

	@Query("select min(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id) from CodeAudit ca where ca.auditor.id = :id")
	Double minNumberOfAuditRecordsPerAudits(int id);

	@Query("select max(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id) from CodeAudit ca where ca.auditor.id = :id")
	Double maxNumberOfAuditRecordsPerAudits(int id);

	@Query("select avg(time_to_sec(timediff(ar.finalPeriod,ar.initialPeriod))/60) from AuditRecord ar where ar.codeAudit.auditor.id = :id")
	Double avgPeriodOfAuditRecords(int id);

	@Query("select stddev(time_to_sec(timediff(ar.finalPeriod,ar.initialPeriod))/60) from AuditRecord ar where ar.codeAudit.auditor.id = :id")
	Double devPeriodOfAuditRecords(int id);

	@Query("select min(time_to_sec(timediff(ar.finalPeriod,ar.initialPeriod))/60) from AuditRecord ar where ar.codeAudit.auditor.id = :id")
	Double minPeriodOfAuditRecords(int id);

	@Query("select max(time_to_sec(timediff(ar.finalPeriod,ar.initialPeriod))/60) from AuditRecord ar where ar.codeAudit.auditor.id = :id")
	Double maxPeriodOfAuditRecords(int id);

}
