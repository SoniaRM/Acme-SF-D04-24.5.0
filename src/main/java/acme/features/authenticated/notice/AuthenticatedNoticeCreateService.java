
package acme.features.authenticated.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.Notice;

@Service
public class AuthenticatedNoticeCreateService extends AbstractService<Authenticated, Notice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedNoticeRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Notice object;

		Principal p = super.getRequest().getPrincipal();
		String username = p.getUsername();
		UserAccount ua = this.repository.findUserAccountById(p.getAccountId());
		String fullname = ua.getIdentity().getFullName();

		object = new Notice();
		object.setInstantiationMoment(MomentHelper.getCurrentMoment());
		object.setAuthor(username + "-" + fullname);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Notice object) {
		assert object != null;

		super.bind(object, "title", "message", "email", "link");

	}

	@Override
	public void validate(final Notice object) {
		assert object != null;

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);

		if (!super.getBuffer().getErrors().hasErrors("confirmation"))
			super.state(confirmation, "confirmation", "authenticated.notice.form.error.confirmation");
	}

	@Override
	public void perform(final Notice object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Notice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "instantiationMoment", "title", "author", "message", "email", "link");

		super.getResponse().addData(dataset);
	}

}
