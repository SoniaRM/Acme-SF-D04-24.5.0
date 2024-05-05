
package acme.features.administrator.objective;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.Administrator;
import acme.client.repositories.AbstractRepository;

@Repository
public interface AdministratorObjectiveRepository extends AbstractRepository {

	@Query("select a from Administrator a where a.id = :id")
	Administrator findOneAdministratorById(int id);

}
