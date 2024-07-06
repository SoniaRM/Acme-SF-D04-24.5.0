
package acme.features.client.progressLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;

@Repository
public interface ClientProgressLogRepository extends AbstractRepository {

	@Query("select c from Contract c where c.id = :id")
	Contract findOneContractById(int id);

	@Query("select pl from ProgressLog pl where pl.recordId = :recordId")
	ProgressLog findOneProgressLogByCode(String recordId);

	@Query("select pl from ProgressLog pl where pl.id = :progressLogId")
	ProgressLog findOneProgressLogById(int progressLogId);

	@Query("select c from Contract c")
	Collection<Contract> findAllContracts();

	@Query("select pl from ProgressLog pl where pl.contract.id = :masterId")
	Collection<ProgressLog> findManyProgressLogsbyContractId(int masterId);

	@Query("select pl.contract from ProgressLog pl where pl.id = :progressLogId")
	Contract findOneContractByProgressLogId(int progressLogId);

}
