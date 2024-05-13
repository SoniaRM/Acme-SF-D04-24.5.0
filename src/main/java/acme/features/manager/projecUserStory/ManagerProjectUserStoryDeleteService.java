
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
		int id;
		ProjectUserStory object;
		Manager manager;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectUserStoryById(id);
		manager = object == null ? null : object.getProject().getManager();

		System.out.println(object);
		status = object != null && object.getProject().isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProjectUserStory object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectUserStoryById(id);

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
		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(project.isDraftMode(), "project", "manager.project-user-story.form.error.project");
		if (!super.getBuffer().getErrors().hasErrors("userStory"))
			super.state(userStory.getManager().equals(project.getManager()), "userStory", "manager.project-user-story.form.error.same-manager");
		Boolean state;
		if (object.getProject() != null && object.getUserStory() != null && !super.getBuffer().getErrors().hasErrors("*")) {
			state = !this.repository.findRelationByProjectIdAndUserStoryId(object.getProject().getId(), object.getUserStory().getId()).isEmpty();
			super.state(state, "*", "manager.relation.form.error.not-exist-relation");
		}
	}

	@Override
	public void perform(final ProjectUserStory object) {
		assert object != null;

		this.repository.delete(object);
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
