
package acme.features.developer.trainingSession;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.TrainingModule;
import acme.entities.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionUpdateService extends AbstractService<Developer, TrainingSession> {

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
		status = super.getRequest().getPrincipal().hasRole(trainingModule.getDeveloper()) || object != null && !object.isDraftMode();

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
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "email", "link");
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingSession existing;

			existing = this.repository.findOneTrainingSessionByCode(object.getCode());
			super.state(existing == null, "code", "developer.training-session.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("startPeriod") && object.getStartPeriod() != null) {

			Date maxStartPeriod;
			maxStartPeriod = MomentHelper.parse("2200/12/31 23:59", "yyyy/MM/dd HH:mm");

			Date minEndPeriod;
			minEndPeriod = MomentHelper.deltaFromMoment(object.getTrainingModule().getCreationMoment(), 7, ChronoUnit.DAYS);

			super.state(MomentHelper.isAfterOrEqual(object.getStartPeriod(), minEndPeriod), "startPeriod", "developer.training-session.form.error.not-one-week-ahead-trainingModule");
			super.state(MomentHelper.isBeforeOrEqual(object.getStartPeriod(), maxStartPeriod), "startPeriod", "developer.training-session.form.error.invalid-date");

		}

		if (!super.getBuffer().getErrors().hasErrors("endPeriod") && object.getEndPeriod() != null) {

			Date maxStartPeriod;
			maxStartPeriod = MomentHelper.parse("2200/12/31 23:59", "yyyy/MM/dd HH:mm");

			Date minEndPeriod;
			minEndPeriod = MomentHelper.deltaFromMoment(object.getStartPeriod(), 7, ChronoUnit.DAYS);

			super.state(MomentHelper.isAfter(object.getEndPeriod(), minEndPeriod), "endPeriod", "developer.training-session.form.error.not-one-week-long");
			super.state(MomentHelper.isAfterOrEqual(object.getEndPeriod(), object.getStartPeriod()), "endPeriod", "developer.training-session.form.error.invalidEndPeriod");
			super.state(MomentHelper.isBeforeOrEqual(object.getEndPeriod(), maxStartPeriod), "endPeriod", "developer.training-session.form.error.invalid-date");
		}
	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Dataset dataset;
		Collection<TrainingModule> trainingModules;
		SelectChoices choices;

		trainingModules = this.repository.findManyTrainingModulesAvailable2();
		choices = SelectChoices.from(trainingModules, "code", object.getTrainingModule());

		dataset = super.unbind(object, "code", "startPeriod", "endPeriod", "location", "instructor", "email", "link");
		dataset.put("trainingModule", choices.getSelected().getKey());
		dataset.put("trainingModules", choices);
		super.getResponse().addData(dataset);
	}

}
