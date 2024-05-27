
package acme.features.client.progressLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogShowService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ClientProgressLogRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int progressLogId;
		ProgressLog object;
		Contract contract;

		progressLogId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProgressLogById(progressLogId);
		contract = object == null ? null : object.getContract();
		status = super.getRequest().getPrincipal().hasRole(contract.getClient()) || object != null && !object.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProgressLogById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;
		Collection<Contract> contracts;
		SelectChoices choices;

		contracts = this.repository.findAllContracts();
		choices = SelectChoices.from(contracts, "code", object.getContract());

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "draftMode");
		dataset.put("contract", choices.getSelected().getKey());
		dataset.put("contracts", choices);
		super.getResponse().addData(dataset);
	}

}
