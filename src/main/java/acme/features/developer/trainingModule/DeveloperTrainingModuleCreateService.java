
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Project;
import acme.entities.TrainingModule;
import acme.enumerated.DifficultyLevel;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleCreateService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private DeveloperTrainingModuleRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		final boolean status;
		status = super.getRequest().getPrincipal().hasRole(Developer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		Developer developer;

		developer = this.repository.findOneDeveloperById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new TrainingModule();
		object.setDeveloper(developer);
		object.setDraftMode(true);
		object.setCreationMoment(MomentHelper.getCurrentMoment());
		object.setUpdateMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		int projectId;
		Project project;
		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		object.setCreationMoment(MomentHelper.getCurrentMoment());
		object.setUpdateMoment(MomentHelper.getCurrentMoment());
		super.bind(object, "code", "details", "difficultyLevel", "link", "estimatedTotalTime");

		object.setProject(project);

	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingModule existing;

			existing = this.repository.findOneTrainingModuleByCode(object.getCode());
			super.state(existing == null, "code", "developer.training-module.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("updateMoment") && object.getUpdateMoment() != null)
			super.state(MomentHelper.isAfterOrEqual(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "developer.training-module.form.error.invalidUpdateMoment");

	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;
		object.setDraftMode(true);
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
		dataset.put("developer", object.getDeveloper());
		dataset.put("draftMode", object.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
