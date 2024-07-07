
package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectShowService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private ManagerProjectRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project object;
		Manager manager;

		projectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(projectId);
		manager = object == null ? null : object.getManager();
		status = super.getRequest().getPrincipal().hasRole(manager) && object != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "title", "abstractProject", "indication", "cost", "link", "draftMode");
		dataset.put("manager", object.getManager());
		super.getResponse().addData(dataset);

		int projectId;
		Project p;
		final boolean showP;
		Manager manager;

		projectId = super.getRequest().getData("id", int.class);
		p = this.repository.findOneProjectById(projectId);
		manager = object == null ? null : p.getManager();
		showP = !p.isDraftMode() && super.getRequest().getPrincipal().getActiveRoleId() == manager.getId();

		super.getResponse().addGlobal("id", projectId);
		super.getResponse().addGlobal("showP", showP);

	}

}
