
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
		int masterId;
		Project project;

		masterId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(masterId);
		status = project != null && super.getRequest().getPrincipal().hasRole(project.getManager()) && project.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProjectUserStory object;
		int masterId;
		Project project;

		masterId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(masterId);

		object = new ProjectUserStory();
		object.setProject(project);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProjectUserStory object) {

		assert object != null;
		int userStoryId;
		UserStory userStory;
		userStoryId = super.getRequest().getData("userStory", int.class);
		userStory = this.repository.findOneUserStoryById(userStoryId);
		object.setUserStory(userStory);
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
		if (!super.getBuffer().getErrors().hasErrors("userStory")) {
			super.state(!userStory.isDraftMode(), "userStory", "manager.project-user-story.form.error.user-story");
			super.state(userStory.getManager().equals(project.getManager()), "userStory", "manager.project-user-story.form.error.same-manager");
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
		Collection<UserStory> userStories;
		SelectChoices choices;
		int masterId;
		Project project;

		masterId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(masterId);

		userStories = this.repository.findManyAvailableUserStoriesToAdd(project.getManager(), project);
		choices = SelectChoices.from(userStories, "title", object.getUserStory());

		dataset = new Dataset();
		dataset.put("userStory", choices.getSelected().getKey());
		dataset.put("userStories", choices);
		dataset.put("masterId", masterId);
		dataset.put("draftMode", project.isDraftMode());
		super.getResponse().addData(dataset);
	}
}
