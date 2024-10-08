
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
		int projectId;
		Project project;
		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		super.bind(object, "code", "details", "difficultyLevel", "link", "estimatedTotalTime");
		object.setProject(project);

	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		Collection<TrainingSession> trainingSessions = this.repository.findManyTrainingSessionsByTrainingModuleId(object.getId());

		boolean trainingSessionsPublished = true;
		for (TrainingSession ts : trainingSessions)
			trainingSessionsPublished = trainingSessionsPublished && !ts.isDraftMode();

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(trainingSessionsPublished, "project", "developer.training-module.form.error.trainingSessionInDraftMode");

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(!trainingSessions.isEmpty(), "project", "developer.training-module.form.error.trainingModuleWithOutTrainingSessions");

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
		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "estimatedTotalTime");
		dataset.put("difficultyLevel", choicesLevel);
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("draftMode", object.isDraftMode());
		super.getResponse().addData(dataset);
	}
}
