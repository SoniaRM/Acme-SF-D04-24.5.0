
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.AuditRecord;
import acme.entities.CodeAudit;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("select ar from AuditRecord ar where ar.codeAudit.auditor.id = :id")
	Collection<AuditRecord> findManyAuditRecordsByAuditorId(int id);

	@Query("select ca from CodeAudit ca where ca.id = :id")
	CodeAudit findOneCodeAuditById(int id);

	@Query("select ar from AuditRecord ar where ar.code = :code")
	AuditRecord findOneAuditRecordByCode(String code);

	@Query("SELECT ca FROM CodeAudit ca JOIN ca.project p WHERE ca.draftMode = true AND p.draftMode = true")
	Collection<CodeAudit> findManyCodeAuditsAvailable();

	@Query("select ar from AuditRecord ar where ar.id = :id")
	AuditRecord findOneAuditRecordById(int id);

	@Query("select ca from CodeAudit ca")
	Collection<CodeAudit> findAllCodeAudits();

	@Query("select ca from CodeAudit ca where ca.draftMode = true")
	Collection<CodeAudit> findManyCodeAuditAvailable2();

	@Query("select ar from AuditRecord ar where ar.codeAudit.id = :codeAuditId")
	Collection<AuditRecord> findManyAuditRecordsByCodeAuditId(int codeAuditId);

}
