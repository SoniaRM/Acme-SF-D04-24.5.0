
package acme.features.authenticated.notice;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.Notice;

@Service
public class AuthenticatedNoticeListService extends AbstractService<Authenticated, Notice> {

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
		Collection<Notice> objects;
		objects = this.repository.findNoticesInLastMonth(MomentHelper.deltaFromMoment(MomentHelper.getCurrentMoment(), -1, ChronoUnit.MONTHS));
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Notice object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "instantiationMoment", "title", "author", "message");
		super.getResponse().addData(dataset);
	}

}
