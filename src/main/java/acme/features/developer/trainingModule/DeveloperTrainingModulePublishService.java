
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Project;
import acme.entities.TrainingModule;
import acme.entities.TrainingSession;
import acme.enumerated.DifficultyLevel;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModulePublishService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private DeveloperTrainingModuleRepository repository;


	@Override
	public void authorise() {
		final boolean status;
		int trainingModuleId;
		TrainingModule object;
		Developer developer;

		trainingModuleId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingModuleById(trainingModuleId);
		developer = object == null ? null : object.getDeveloper();
		status = object != null && object.isDraftMode() && super.getRequest().getPrincipal().hasRole(developer);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "estimatedTotalTime", "project");
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;
		boolean draftModeTrainingSession;

		final Collection<TrainingSession> trainingSessions = this.repository.findManyTrainingSessionsByTrainingModuleId(object.getId());
		super.state(!trainingSessions.isEmpty(), "*", "developer.training-module.form.error.trainingModuleWithOutTrainingSessions");
		if (!trainingSessions.isEmpty()) {
			draftModeTrainingSession = trainingSessions.stream().anyMatch(x -> !x.isDraftMode());
			super.state(draftModeTrainingSession, "*", "developer.training-module.form.error.trainingSessionInDraftMode");
		}
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;
		Collection<Project> projects;
		SelectChoices choices;
		SelectChoices choicesLevel;

		projects = this.repository.findManyProjectsAvailable();

		choicesLevel = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		choices = SelectChoices.from(projects, "title", object.getProject());

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "estimatedTotalTime", "draftMode", "project");
		dataset.put("difficultyLevel", choicesLevel);
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		super.getResponse().addData(dataset);
	}
}
