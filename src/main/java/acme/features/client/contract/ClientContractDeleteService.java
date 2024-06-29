
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Project;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Service
public class ClientContractDeleteService extends AbstractService<Client, Contract> {

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
		Collection<ProgressLog> progresslogs = this.repository.findManyProgressLogsByContractId(object.getId());

		boolean progressLogsPublished = true;
		for (ProgressLog pl : progresslogs)
			progressLogsPublished = progressLogsPublished && !pl.isDraftMode();

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(progressLogsPublished, "project", "client.contract.form.error.progress-logs-published");
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		Collection<ProgressLog> progressLogs;

		progressLogs = this.repository.findManyProgressLogsByContractId(object.getId());
		this.repository.deleteAll(progressLogs);
		this.repository.delete(object);

	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choices;

		projects = this.repository.findManyProjectsAvailable();

		choices = SelectChoices.from(projects, "title", object.getProject());

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("draftMode", object.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
