
package acme.features.auditor.codeAudit;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.AuditRecord;
import acme.entities.CodeAudit;
import acme.entities.Project;
import acme.roles.Auditor;

@Repository
public interface AuditorCodeAuditRepository extends AbstractRepository {

	@Query("select ca from CodeAudit ca")
	Collection<CodeAudit> findAllCodeAudits();

	@Query("select ca from CodeAudit ca where ca.id = :id")
	CodeAudit findOneCodeAuditById(int id);

	//
	@Query("select p from Project p where p.id = :id and p.draftMode = false")
	Project findOnePublishedProjectById(int id);
	//

	//
	@Query("select p.draftMode from Project p where p.id = :projectId")
	Boolean projectIsDraftMode(int projectId);
	//

	//
	@Query("select min(ar.initialPeriod) from AuditRecord ar where ar.codeAudit.id = :codeAuditId")
	Date findValidExecutionDateBeforeInitial(int codeAuditId);
	//

	@Query("select ca from CodeAudit ca where ca.auditor.id = :auditorId")
	Collection<CodeAudit> findManyCodeAuditsByAuditorId(int auditorId);

	@Query("select ca from CodeAudit ca where ca.code = :code")
	CodeAudit findOneCodeAuditByCode(String code);

	@Query("select a from Auditor a where a.id = :id")
	Auditor findOneAuditorById(int id);

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findManyProjectsAvailable();

	@Query("select ar from AuditRecord ar where ar.codeAudit.id = :codeAuditId")
	Collection<AuditRecord> findManyAuditRecordsByCodeAuditId(int codeAuditId);

	@Query("Select ca.code from CodeAudit ca")
	Collection<String> findAllCodes();

}
