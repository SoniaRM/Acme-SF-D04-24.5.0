
package acme.features.developer.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {

	@Autowired
	private DeveloperDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Developer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		id = super.getRequest().getPrincipal().getActiveRoleId();
		DeveloperDashboard dashboard;

		int trainingModules = this.repository.findPublishedTrainingModulesByDeveloperId(id);

		int totalTrainingModulesWithUpdateMoment;
		int totalTrainingSessionsWithLink;

		Double avgTimeTrainingModule;
		Double devTimeTrainingModule;
		Double minTimeTrainingModule;
		Double maxTimeTrainingModule;

		totalTrainingModulesWithUpdateMoment = this.repository.totalTrainingModulesWithUpdateMoment(id);
		totalTrainingSessionsWithLink = this.repository.totalTrainingSessionsWithLink(id);

		avgTimeTrainingModule = this.repository.avgTimeTrainingModule(id);

		System.out.println(trainingModules);

		if (trainingModules > 1)
			devTimeTrainingModule = this.repository.devTimeTrainingModule(id);
		else
			devTimeTrainingModule = null;

		minTimeTrainingModule = this.repository.minTimeTrainingModule(id);
		maxTimeTrainingModule = this.repository.maxTimeTrainingModule(id);

		dashboard = new DeveloperDashboard();
		dashboard.setTotalTrainingModulesWithUpdateMoment(totalTrainingModulesWithUpdateMoment);
		dashboard.setTotalTrainingSessionsWithLink(totalTrainingSessionsWithLink);

		dashboard.setAvgTimeTrainingModule(avgTimeTrainingModule);
		dashboard.setDevTimeTrainingModule(devTimeTrainingModule);
		dashboard.setMinTimeTrainingModule(minTimeTrainingModule);
		dashboard.setMaxTimeTrainingModule(maxTimeTrainingModule);

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"totalTrainingModulesWithUpdateMoment", "totalTrainingSessionsWithLink", // 
			"avgTimeTrainingModule", "devTimeTrainingModule", //
			"minTimeTrainingModule", "maxTimeTrainingModule");

		super.getResponse().addData(dataset);
	}

}
