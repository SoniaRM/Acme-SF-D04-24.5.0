
package acme.features.client.progressLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
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
		Client client;

		progressLogId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProgressLogById(progressLogId);
		contract = object == null ? null : object.getContract();
		client = contract == null ? null : contract.getClient();
		status = object != null && super.getRequest().getPrincipal().hasRole(client);

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

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");
		dataset.put("masterId", object.getContract().getId());
		dataset.put("draftMode", object.isDraftMode());
		dataset.put("contract", object.getContract().getCode());

		super.getResponse().addData(dataset);
	}

}
