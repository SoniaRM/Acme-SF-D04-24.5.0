
package acme.features.authenticated.notice;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.Notice;

@Service
public class AuthenticatedNoticeShowService extends AbstractService<Authenticated, Notice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedNoticeRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int noticeId;
		Notice notice;
		Date lastMonth;

		noticeId = super.getRequest().getData("id", int.class);
		notice = this.repository.findOneNoticeById(noticeId);

		lastMonth = MomentHelper.deltaFromMoment(MomentHelper.getCurrentMoment(), -1, ChronoUnit.MONTHS);
		status = notice != null && notice.getInstantiationMoment().after(lastMonth);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Notice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneNoticeById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Notice object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "instantiationMoment", "title", "author", "message", "email", "link");
		super.getResponse().addData(dataset);
	}

}
