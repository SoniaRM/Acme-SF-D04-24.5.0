
package acme.features.client.progressLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogUpdateService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ClientProgressLogRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int progressLogId;
		Contract contract;
		ProgressLog progressLog;

		progressLogId = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findOneProgressLogById(progressLogId);
		contract = this.repository.findOneContractByProgressLogId(progressLogId);
		status = contract != null && progressLog.isDraftMode() && super.getRequest().getPrincipal().hasRole(contract.getClient());

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
	public void bind(final ProgressLog object) {
		assert object != null;

		super.bind(object, "recordId", "completeness", "comment", "responsiblePerson");
	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {

			ProgressLog progressLogWithCodeDuplicated = this.repository.findOneProgressLogByCode(object.getRecordId());

			if (progressLogWithCodeDuplicated != null)
				super.state(progressLogWithCodeDuplicated.getId() == object.getId(), "recordId", "client.progress-log.form.error.duplicated");
		}

	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		this.repository.save(object);
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
