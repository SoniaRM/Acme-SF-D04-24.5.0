
package acme.features.manager.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.enumerated.Priority;
import acme.roles.Manager;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select m from Manager m where m.userAccount.id = :id")
	Manager findOneManagerByUserAccountId(int id);

	@Query("select count(us) from UserStory us where us.priority = :priority AND us.draftMode = false AND us.manager.id = :id ")
	int totalUserStoriesWithPriority(Priority priority, int id);

	@Query("select avg(us.estimatedCost) from UserStory us where us.draftMode = false AND us.manager.id = :id")
	Double avgEstimatedCostUserStory(int id);

	@Query("select SQRT((SUM(us.estimatedCost * us.estimatedCost) / COUNT(us.estimatedCost)) - (AVG(us.estimatedCost) * AVG(us.estimatedCost))) from UserStory us where us.draftMode = false AND us.manager.id = :id")
	Double devEstimatedCostUserStory(int id);

	@Query("select min(us.estimatedCost) from UserStory us where us.draftMode = false AND us.manager.id = :id")
	Double minEstimatedCostUserStory(int id);

	@Query("select max(us.estimatedCost) from UserStory us where us.draftMode = false AND us.manager.id = :id")
	Double maxEstimatedCostUserStory(int id);

	@Query("select avg(p.cost) from Project p where p.draftMode = false AND p.manager.id = :id")
	Double avgCostProject(int id);

	@Query("select SQRT((SUM(p.cost * p.cost) / COUNT(p.cost)) - (AVG(p.cost) * AVG(p.cost))) from Project p where p.draftMode = false AND p.manager.id = :id")
	Double devCostProject(int id);

	@Query("select min(p.cost) from Project p where p.draftMode = false AND p.manager.id = :id")
	Double minCostProject(int id);

	@Query("select max(p.cost) from Project p where p.draftMode = false AND p.manager.id = :id")
	Double maxCostProject(int id);

	@Query("select count(p) from Project p where (p.manager.id = :id and p.draftMode = false)")
	int findPublishedProjectsByManagerId(int id);

	@Query("select count(us) from UserStory us where (us.manager.id = :id and us.draftMode = false)")
	int findPublishedUserStoriesByManagerId(int id);

}
