
package acme.features.developer.trainingSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.TrainingModule;
import acme.entities.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionShowService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private DeveloperTrainingSessionRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int trainingSessionId;
		TrainingSession object;
		TrainingModule trainingModule;

		trainingSessionId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingSessionById(trainingSessionId);
		trainingModule = object == null ? null : object.getTrainingModule();
		status = super.getRequest().getPrincipal().hasRole(trainingModule.getDeveloper()) && object != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingSession object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingSessionById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "email", "link");
		dataset.put("masterId", object.getTrainingModule().getId());
		dataset.put("draftMode", object.isDraftMode());
		dataset.put("trainingModule", object.getTrainingModule().getCode());

		super.getResponse().addData(dataset);
	}

}
