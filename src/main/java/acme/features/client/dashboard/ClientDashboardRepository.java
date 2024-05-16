
package acme.features.client.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.roles.Client;

@Repository
public interface ClientDashboardRepository extends AbstractRepository {

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 0.25")
	int findTotalLogLessThan25(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 0.50 and 0.25 <= pl.completeness")
	int findTotalLogLessBetween25And50(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and pl.completeness < 0.75 and 0.50 <= pl.completeness")
	int findTotalLogLessBetween50And75(Client client);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client = :client and 0.75 <= pl.completeness")
	int findTotalLogAbove75(Client client);

	@Query("select avg(c.budget.amount) from Contract c where c.client = :client")
	Double findAverageBudgetContracts(Client client);

	@Query("select stddev(c.budget.amount) from Contract c where c.client = :client")
	Double findDeviationBudgetContracts(Client client);

	@Query("select min(c.budget.amount) from Contract c where c.client = :client")
	Double findMinimunBudgetContracts(Client client);

	@Query("select max(c.budget.amount) from Contract c where c.client = :client")
	Double findMaximumBudgetContracts(Client client);

	@Query("select c from Client c where c.userAccount.id = :id")
	Client findOneClientByUserAccountId(int id);

}
