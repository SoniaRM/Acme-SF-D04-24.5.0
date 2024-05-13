
package acme.features.manager.projecUserStory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.ProjectUserStory;
import acme.roles.Manager;

@Controller
public class ManagerProjectUserStoryController extends AbstractController<Manager, ProjectUserStory> {

	@Autowired
	private ManagerProjectUserStoryListService		listService;

	@Autowired
	private ManagerProjectUserStoryShowService		showService;

	@Autowired
	private ManagerProjectUserStoryCreateService	createService;

	@Autowired
	private ManagerProjectUserStoryDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);

	}

}
