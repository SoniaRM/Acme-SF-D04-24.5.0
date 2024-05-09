
package acme.features.manager.projecUserStory;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Project;
import acme.entities.ProjectUserStory;
import acme.entities.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerProjectUserStoryRepository extends AbstractRepository {

	@Query("select p from Project p where p.id = :masterId")
	Project findOneProjectById(int masterId);

	@Query("select us from UserStory us where us.id = :id")
	UserStory findOneUserStoryById(int id);

	@Query("select us from UserStory us where us.manager = :manager")
	Collection<UserStory> findManyUserStoriesAvailableByManager(Manager manager);

	@Query("select pus.userStory from ProjectUserStory pus where pus.userStory.manager = :manager and pus.project.code = :code")
	Collection<UserStory> findManyUserStoriesNotAvailableByManagerAndProject(Manager manager, String code);

	default Collection<UserStory> findManyAvailableUserStoriesToAdd(final Manager manager, final String code) {
		Collection<UserStory> objects;
		objects = this.findManyUserStoriesAvailableByManager(manager);
		objects.removeAll(this.findManyUserStoriesNotAvailableByManagerAndProject(manager, code));
		return objects;
	}

	@Query("select pus from ProjectUserStory pus where pus.id = :id")
	ProjectUserStory findOneProjectUserStoryById(int id);

	@Query("select pus from ProjectUserStory pus where pus.userStory.manager = :manager and pus.project = :project")
	Collection<ProjectUserStory> findManyProjectUserStoryByProjectAndManager(Manager manager, Project project);

}
