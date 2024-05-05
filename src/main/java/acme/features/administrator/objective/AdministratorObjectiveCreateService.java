
package acme.features.administrator.objective;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.Objective;
import acme.enumerated.ObjectivePriority;

@Service
public class AdministratorObjectiveCreateService extends AbstractService<Administrator, Objective> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorObjectiveRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Objective object;
		Administrator administrator;

		administrator = this.repository.findOneAdministratorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Objective();
		object.setInstantiationMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Objective object) {
		assert object != null;

		super.bind(object, "instantiationMoment", "title", "description", "priority", "status", "durationStart", "durationEnd", "link");

	}

	@Override
	public void validate(final Objective object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("durationStart") && object.getDurationStart() != null)
			super.state(MomentHelper.isAfter(object.getDurationStart(), object.getInstantiationMoment()), "durationStart", "administrator.objective.form.error.invalidDurationStart");
		if (!super.getBuffer().getErrors().hasErrors("durationEnd") && object.getDurationStart() != null && object.getDurationEnd() != null)
			super.state(MomentHelper.isAfter(object.getDurationEnd(), object.getDurationStart()), "durationEnd", "administrator.objective.form.error.invalidDurationEnd");

	}

	@Override
	public void perform(final Objective object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Objective object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choicesPriority;

		choicesPriority = SelectChoices.from(ObjectivePriority.class, object.getPriority());

		dataset = super.unbind(object, "instantiationMoment", "title", "description", "priority", "status", "durationStart", "durationEnd", "link");
		dataset.put("priority", choicesPriority);
		dataset.put("priorities", choicesPriority);

		super.getResponse().addData(dataset);
	}

}
