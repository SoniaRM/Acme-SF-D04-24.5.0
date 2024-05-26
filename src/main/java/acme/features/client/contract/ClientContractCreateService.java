
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Project;
import acme.entities.contracts.Contract;
import acme.roles.Client;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ClientContractRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		final boolean status;
		status = super.getRequest().getPrincipal().hasRole(Client.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		Client client;

		client = this.repository.findOneClientById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Contract();
		object.setInstantiationMoment(MomentHelper.getCurrentMoment());
		object.setClient(client);
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		super.bind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project");

	}

	@Override
	public void validate(final Contract object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;

			existing = this.repository.findOneContractByCode(object.getCode());
			super.state(existing == null, "code", "client.contract.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() > 0, "budget", "client.contract.form.error.negative-budget");

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() <= 1000000, "budget", "client.contract.form.error.over-budget");

	}

	@Override
	public void perform(final Contract object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choices;

		projects = this.repository.findManyProjectsAvailable();

		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("client", object.getClient());

		super.getResponse().addData(dataset);
	}
}
