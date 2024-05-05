
package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectUpdateService extends AbstractService<Manager, Project> {

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Project object;
		Manager manager;

		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Project();
		object.setManager(manager);
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;
		super.bind(object, "code", "title", "abstractProject", "indication", "cost", "link", "draftMode");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {

			Project projectWithCodeDuplicated = this.repository.findOneProjectByCode(object.getCode());

			if (projectWithCodeDuplicated != null)
				super.state(projectWithCodeDuplicated.getId() == object.getId(), "code", "manager.project.form.error.code");
		}

	}

	@Override
	public void perform(final Project object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "title", "abstractProject", "indication", "cost", "link", "draftMode");
		super.getResponse().addData(dataset);
	}

}
