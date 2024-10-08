
package acme.features.manager.userStory;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Project;
import acme.entities.ProjectUserStory;
import acme.entities.UserStory;
import acme.roles.Manager;

@Service
public class ManagerUserStoryListMineService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		int masterId;
		Project project;
		Manager manager;
		masterId = super.getRequest().getData("masterId", int.class);

		project = this.repository.findOneProjectById(masterId);
		manager = project == null ? null : project.getManager();
		status = project != null && (!project.isDraftMode() || super.getRequest().getPrincipal().hasRole(project.getManager())) && super.getRequest().getPrincipal().getActiveRoleId() == manager.getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		Collection<ProjectUserStory> objects;
		objects = this.repository.findManyProjectUserStoriesByProjectId(masterId);

		Collection<UserStory> userStories;
		userStories = objects.stream().map(ProjectUserStory::getUserStory).collect(Collectors.toList());

		int projectId;
		Project project;

		projectId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(projectId);
		super.getResponse().addGlobal("project", project);

		super.getBuffer().addData(userStories);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link", "draftMode");
		String draftMode = object.isDraftMode() ? "✓" : "x";
		dataset.put("draftMode", draftMode);

		super.getResponse().addData(dataset);

	}

}
