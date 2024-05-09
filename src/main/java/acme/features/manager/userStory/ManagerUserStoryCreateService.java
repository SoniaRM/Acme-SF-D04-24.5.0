
package acme.features.manager.userStory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.UserStory;
import acme.enumerated.Priority;
import acme.roles.Manager;

@Service
public class ManagerUserStoryCreateService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ManagerUserStoryRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		final boolean status;
		status = super.getRequest().getPrincipal().hasRole(Manager.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		Manager manager;
		object = new UserStory();

		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setDraftMode(true);
		object.setManager(manager);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link");
	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;
	}

	@Override
	public void perform(final UserStory object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choicesPriority;

		choicesPriority = SelectChoices.from(Priority.class, object.getPriority());

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link", "draftMode");
		dataset.put("priority", choicesPriority.getSelected().getKey());
		dataset.put("priorities", choicesPriority);
		dataset.put("manager", object.getManager());

		super.getResponse().addData(dataset);
	}
}
