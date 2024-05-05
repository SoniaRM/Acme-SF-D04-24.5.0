
package acme.features.administrator.systemConfiguration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.client.data.accounts.Administrator;
import acme.entities.SystemConfiguration;

@Controller
public class AdministratorSystemConfigurationController extends AbstractController<Administrator, SystemConfiguration> {

	@Autowired
	protected AdministratorSystemConfigurationUpdateService	updateService;

	@Autowired
	protected AdministratorSystemConfigurationShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);

	}

}
