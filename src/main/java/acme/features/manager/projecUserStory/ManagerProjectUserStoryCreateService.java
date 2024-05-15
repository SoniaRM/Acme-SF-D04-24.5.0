
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
public class ManagerProjectUserStoryCreateService extends AbstractService<Manager, ProjectUserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerProjectUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Manager.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProjectUserStory object;
		object = new ProjectUserStory();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProjectUserStory object) {

		assert object != null;
		super.bind(object, "project", "userStory");

	}

	@Override
	public void validate(final ProjectUserStory object) {
		assert object != null;
		Project project;
		UserStory userStory;
		project = object.getProject();
		userStory = object.getUserStory();
		super.state(object.getUserStory() != null, "userStory", "manager.project-user-story.form.error.user-story-must-not-be-null");

		if (object.getProject() == null)
			super.state(object.getProject() != null, "project", "manager.project-user-story.form.error.project-must-not-be-null");
		else {
			if (!super.getBuffer().getErrors().hasErrors("project"))
				super.state(project.isDraftMode(), "project", "manager.project-user-story.form.error.project");
			Boolean state;
			if (object.getProject() != null && object.getUserStory() != null && !super.getBuffer().getErrors().hasErrors("*")) {
				state = this.repository.findRelationByProjectIdAndUserStoryId(object.getProject().getId(), object.getUserStory().getId()).isEmpty();
				super.state(state, "*", "manager.relation.form.error.existing-relation");
			}
			if (!super.getBuffer().getErrors().hasErrors("userStory"))
				super.state(userStory.getManager().equals(project.getManager()), "userStory", "manager.project-user-story.form.error.not-same-manager");

		}

	}

	@Override
	public void perform(final ProjectUserStory object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		Collection<UserStory> userStories;
		SelectChoices choicesP;
		SelectChoices choicesUS;
		int id;

		id = super.getRequest().getPrincipal().getActiveRoleId();
		projects = this.repository.findManyProjectsToAddByManager(id);
		userStories = this.repository.findManyUserStoriesToAddByManager(id);

		choicesP = SelectChoices.from(projects, "code", object.getProject());
		choicesUS = SelectChoices.from(userStories, "title", object.getUserStory());

		dataset = super.unbind(object, "project", "userStory");
		dataset.put("projects", choicesP);
		dataset.put("userStories", choicesUS);

		super.getResponse().addData(dataset);

	}
}
