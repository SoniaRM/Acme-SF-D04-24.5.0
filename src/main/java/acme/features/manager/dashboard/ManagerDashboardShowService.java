
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
		Manager manager;
		int id;
		id = super.getRequest().getPrincipal().getActiveRoleId();
		manager = this.repository.findOneManagerByUserAccountId(id);
		ManagerDashboard dashboard;

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
		devEstimatedCostUserStory = this.repository.devEstimatedCostUserStory(id);
		minEstimatedCostUserStory = this.repository.minEstimatedCostUserStory(id);
		maxEstimatedCostUserStory = this.repository.maxEstimatedCostUserStory(id);

		avgCostProject = this.repository.avgCostProject(id);
		devCostProject = this.repository.devCostProject(id);
		minCostProject = this.repository.minCostProject(id);
		maxCostProject = this.repository.maxCostProject(id);

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
