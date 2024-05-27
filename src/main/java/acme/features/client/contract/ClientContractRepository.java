
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Project;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	//List
	@Query("select c from Contract c where c.client.id = :clientId")
	Collection<Contract> findManyContractsByClientId(int clientId);

	@Query("select pl from ProgressLog pl where pl.contract.id = :contractId")
	Collection<ProgressLog> findManyProgressLogsByContractId(int contractId);

	//Show
	@Query("select c from Contract c where c.id = :contractId")
	Contract findOneContractById(int contractId);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findManyProjectsAvailable();

	//Create
	@Query("select cl from Client cl where cl.id = :id")
	Client findOneClientById(int id);

	@Query("select c from Contract c where c.code = :code")
	Contract findOneContractByCode(String code);

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

}
