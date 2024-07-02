
package acme.features.client.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.roles.Client;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ClientContractRepository repository;


	@Override
	public void authorise() {
		final boolean status;
		int contractId;
		Contract object;
		Client client;

		contractId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(contractId);
		client = object == null ? null : object.getClient();
		status = object != null && object.isDraftMode() && super.getRequest().getPrincipal().hasRole(client);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;
	}

	@Override
	public void validate(final Contract object) {
		assert object != null;

	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		//		Dataset dataset;
		//		Collection<Project> projects;
		//		SelectChoices choices;
		//
		//		projects = this.repository.findManyProjectsAvailable();
		//
		//		choices = SelectChoices.from(projects, "title", object.getProject());
		//
		//		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "draftMode", "project");
		//		dataset.put("project", choices.getSelected());
		//		dataset.put("projects", choices);
		//
		//		super.getResponse().addData(dataset);
	}
}
