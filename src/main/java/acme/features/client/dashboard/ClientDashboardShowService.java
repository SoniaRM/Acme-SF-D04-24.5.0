
package acme.features.client.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.roles.Client;

@Service
public class ClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	@Autowired
	protected ClientDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status;
		Principal principal = super.getRequest().getPrincipal();
		int id = principal.getAccountId();

		Client client = this.repository.findOneClientByUserAccountId(id);
		status = client != null && principal.hasRole(Client.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Client client;
		ClientDashboard dashboard;
		Principal principal;
		int id;

		principal = super.getRequest().getPrincipal();
		id = principal.getAccountId();
		//id = super.getRequest().getPrincipal().getActiveRoleId();

		client = this.repository.findOneClientByUserAccountId(id);

		int totalLogLessThan25;
		int totalLogLessBetween25And50;
		int totalLogLessBetween50And75;
		int totalLogAbove75;
		Double averageBudgetContracts;
		Double deviationBudgetContracts;
		Double minimumBudgetContracts;
		Double maximumBudgetContracts;

		totalLogLessThan25 = this.repository.findTotalLogLessThan25(client);
		totalLogLessBetween25And50 = this.repository.findTotalLogLessBetween25And50(client);
		totalLogLessBetween50And75 = this.repository.findTotalLogLessBetween50And75(client);
		totalLogAbove75 = this.repository.findTotalLogAbove75(client);

		averageBudgetContracts = this.repository.findAverageBudgetContracts(client);
		deviationBudgetContracts = this.repository.findDeviationBudgetContracts(client);
		minimumBudgetContracts = this.repository.findMinimunBudgetContracts(client);
		maximumBudgetContracts = this.repository.findMaximumBudgetContracts(client);

		dashboard = new ClientDashboard();

		dashboard.setTotalLogLessThan25(totalLogLessThan25);
		dashboard.setTotalLogLessBetween25And50(totalLogLessBetween25And50);
		dashboard.setTotalLogLessBetween50And75(totalLogLessBetween50And75);
		dashboard.setTotalLogAbove75(totalLogAbove75);

		dashboard.setAverageBudgetContracts(averageBudgetContracts);
		dashboard.setDeviationBudgetContracts(deviationBudgetContracts);
		dashboard.setMinimumBudgetContracts(minimumBudgetContracts);
		dashboard.setMaximumBudgetContracts(maximumBudgetContracts);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "totalLogLessThan25", "totalLogLessBetween25And50", //
			"totalLogLessBetween50And75", "totalLogAbove75", "averageBudgetContracts", //
			"deviationBudgetContracts", "minimumBudgetContracts", "maximumBudgetContracts");
		super.getResponse().addData(dataset);
	}

}
