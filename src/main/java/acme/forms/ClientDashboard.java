
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							totalLogLessThan25;
	int							totalLogLessBetween25And50;
	int							totalLogLessBetween50And75;
	int							totalLogAbove75;

	Double						averageBudgetContracts;
	Double						deviationBudgetContracts;
	Double						minimumBudgetContracts;
	Double						maximumBudgetContracts;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
