
package acme.features.manager.projecUserStory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Project;
import acme.entities.ProjectUserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryListService extends AbstractService<Manager, ProjectUserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ManagerProjectUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Collection<ProjectUserStory> objects;
		int masterId;
		Project project;

		masterId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(masterId);
		objects = this.repository.findManyProjectUserStoryByProjectAndManager(project.getManager(), project);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "project", "userStory", "userStory.description", "userStory.title");

		dataset.put("draftMode", object.getProject().isDraftMode());

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<ProjectUserStory> objects) {
		assert objects != null;

		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		final Project project = this.repository.findOneProjectById(masterId);
		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("draftMode", project.isDraftMode());
	}
}
