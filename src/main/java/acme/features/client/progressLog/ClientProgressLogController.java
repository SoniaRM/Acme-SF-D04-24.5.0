
package acme.features.client.progressLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Controller
public class ClientProgressLogController extends AbstractController<Client, ProgressLog> {

	@Autowired
	private ClientProgressLogListService	listService;

	@Autowired
	private ClientProgressLogShowService	showService;

	@Autowired
	private ClientProgressLogUpdateService	updateService;

	@Autowired
	private ClientProgressLogCreateService	createService;

	@Autowired
	private ClientProgressLogDeleteService	deleteService;

	@Autowired
	private ClientProgressLogPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("list", this.listService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
