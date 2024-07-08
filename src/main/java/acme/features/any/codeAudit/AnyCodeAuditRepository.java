
package acme.features.any.codeAudit;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.CodeAudit;
import acme.entities.Project;

@Repository
public interface AnyCodeAuditRepository extends AbstractRepository {

	@Query("select ca from CodeAudit ca where ca.draftMode = false")
	Collection<CodeAudit> findAllCodeAuditPublished();

	@Query("select ca from CodeAudit ca where ca.id = :id")
	CodeAudit findOneCodeAuditById(int id);

	@Query("select p from Project p where p.draftMode = false")
	List<Project> findPublishedProjects();

}
