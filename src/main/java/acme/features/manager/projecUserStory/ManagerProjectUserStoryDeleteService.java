
package acme.features.manager.projecUserStory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
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
		ProjectUserStory pus;

		id = super.getRequest().getData("id", int.class);
		pus = this.repository.findOneProjectUserStoryById(id);
		status = pus != null && super.getRequest().getPrincipal().hasRole(pus.getProject().getManager()) && pus.getProject().isDraftMode();

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
			//super.state(!userStory.isDraftMode(), "userStory", "manager.project-user-story.form.error.user-story");
			super.state(userStory.getManager().equals(project.getManager()), "userStory", "manager.project-user-story.form.error.same-manager");

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
		int id;
		ProjectUserStory pus;

		id = super.getRequest().getData("id", int.class);
		pus = this.repository.findOneProjectUserStoryById(id);

		dataset = super.unbind(object, "project", "userStory", "userStory.title", "userStory.description", "userStory.estimatedCost", "userStory.acceptanceCriteria", "userStory.priority", "userStory.link");
		dataset.put("draftMode", pus.getProject().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
