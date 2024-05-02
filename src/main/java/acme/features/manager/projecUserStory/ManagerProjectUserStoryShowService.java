
package acme.features.manager.projecUserStory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.ProjectUserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryShowService extends AbstractService<Manager, ProjectUserStory> {

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
		status = pus != null && super.getRequest().getPrincipal().hasRole(pus.getProject().getManager());

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
