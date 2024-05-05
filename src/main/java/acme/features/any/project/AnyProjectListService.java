
package acme.features.any.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Project;

@Service
public class AnyProjectListService extends AbstractService<Any, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Project> objects;
		objects = this.repository.findAllProjectPublished();

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "code", "title", "abstractProject", "indication", "cost", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
