
package acme.features.manager.projecUserStory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Project;
import acme.entities.ProjectUserStory;
import acme.entities.UserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryDeleteService extends AbstractService<Manager, ProjectUserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerProjectUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Manager manager;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("projectId", int.class);
		project = this.repository.findOneProjectById(projectId);
		manager = project == null ? null : project.getManager();
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		ProjectUserStory object;
		Project project;
		int projectId;
		Collection<ProjectUserStory> puss;
		projectId = super.getRequest().getData("projectId", int.class);

		puss = this.repository.findManyProjectUserStoriesByProjectId(projectId);

		if (puss.isEmpty()) {
			project = this.repository.findOneProjectById(projectId);

			object = new ProjectUserStory();
			object.setProject(project);
		}

		else
			object = puss.stream().findFirst().get();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProjectUserStory object) {
		assert object != null;
		int projectId;

		Project project;

		projectId = super.getRequest().getData("projectId", int.class);
		project = this.repository.findOneProjectById(projectId);
		object.setProject(project);

	}

	@Override
	public void validate(final ProjectUserStory object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("userStory")) {
			Collection<UserStory> userStoriesAssigned;

			userStoriesAssigned = this.repository.findManyUserStoriesByProjectId(object.getProject().getId());

			super.state(userStoriesAssigned.contains(object.getUserStory()), "userStory", "manager.project-user-story.form.error.unassigning-user-story-not-assigned-to-project");
			super.state(object.getProject() != null, "project", "manager.project-user-story.form.error.project-is-null");
			if (object.getProject() != null)
				super.state(object.getProject().isDraftMode(), "project", "manager.project-user-story.form.error.project-is-published");
			super.state(object.getUserStory() != null, "userStory", "manager.project-user-story.form.error.user-story-must-not-be-null");

		}
	}

	@Override
	public void perform(final ProjectUserStory object) {
		assert object != null;
		ProjectUserStory relation;
		relation = this.repository.findRelationByProjectIdAndUserStoryId2(object.getProject().getId(), object.getUserStory().getId());
		this.repository.delete(relation);
	}

	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;

		Collection<UserStory> draftModeUserStories;
		SelectChoices choices;
		Dataset dataset;

		draftModeUserStories = this.repository.findManyUserStoriesByProjectId(super.getRequest().getData("projectId", int.class));

		choices = SelectChoices.from(draftModeUserStories, "title", object.getUserStory());

		dataset = super.unbind(object, "project");
		dataset.put("userStory", choices.getSelected().getKey());
		dataset.put("userStories", choices);
		dataset.put("projectId", object.getProject().getId());

		super.getResponse().addData(dataset);
	}

}
