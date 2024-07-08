
package acme.features.manager.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.enumerated.Priority;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Service
public class ManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	@Autowired
	private ManagerDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Manager.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		id = super.getRequest().getPrincipal().getActiveRoleId();
		ManagerDashboard dashboard;

		int projects = this.repository.findPublishedProjectsByManagerId(id);
		int userStories = this.repository.findPublishedUserStoriesByManagerId(id);
		int totalMustUserStory;
		int totalShouldUserStory;
		int totalCouldUserStory;
		int totalWontUserStory;

		Double avgEstimatedCostUserStory;
		Double devEstimatedCostUserStory;
		Double minEstimatedCostUserStory;
		Double maxEstimatedCostUserStory;

		Double avgCostProject;
		Double devCostProject;
		Double minCostProject;
		Double maxCostProject;

		totalMustUserStory = this.repository.totalUserStoriesWithPriority(Priority.MUST, id);
		totalShouldUserStory = this.repository.totalUserStoriesWithPriority(Priority.SHOULD, id);
		totalCouldUserStory = this.repository.totalUserStoriesWithPriority(Priority.COULD, id);
		totalWontUserStory = this.repository.totalUserStoriesWithPriority(Priority.WONT, id);

		avgEstimatedCostUserStory = this.repository.avgEstimatedCostUserStory(id);
		minEstimatedCostUserStory = this.repository.minEstimatedCostUserStory(id);
		maxEstimatedCostUserStory = this.repository.maxEstimatedCostUserStory(id);

		if (userStories > 1)
			devEstimatedCostUserStory = this.repository.devEstimatedCostUserStory(id);
		else
			devEstimatedCostUserStory = null;

		avgCostProject = this.repository.avgCostProject(id);
		minCostProject = this.repository.minCostProject(id);
		maxCostProject = this.repository.maxCostProject(id);

		if (projects > 1)
			devCostProject = this.repository.devCostProject(id);
		else
			devCostProject = null;

		dashboard = new ManagerDashboard();
		dashboard.setTotalMustUserStory(totalMustUserStory);
		dashboard.setTotalShouldUserStory(totalShouldUserStory);
		dashboard.setTotalCouldUserStory(totalCouldUserStory);
		dashboard.setTotalWontUserStory(totalWontUserStory);
		dashboard.setAvgEstimatedCostUserStory(avgEstimatedCostUserStory);
		dashboard.setDevEstimatedCostUserStory(devEstimatedCostUserStory);
		dashboard.setMinEstimatedCostUserStory(minEstimatedCostUserStory);
		dashboard.setMaxEstimatedCostUserStory(maxEstimatedCostUserStory);
		dashboard.setAvgCostProject(avgCostProject);
		dashboard.setDevCostProject(devCostProject);
		dashboard.setMinCostProject(minCostProject);
		dashboard.setMaxCostProject(maxCostProject);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"totalMustUserStory", "totalShouldUserStory", // 
			"totalCouldUserStory", "totalWontUserStory", //
			"avgEstimatedCostUserStory", "devEstimatedCostUserStory", //
			"minEstimatedCostUserStory", "maxEstimatedCostUserStory", //
			"avgCostProject", "devCostProject", //
			"minCostProject", "maxCostProject");

		super.getResponse().addData(dataset);
	}

}
