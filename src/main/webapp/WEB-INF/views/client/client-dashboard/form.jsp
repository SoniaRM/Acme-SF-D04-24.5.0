<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogsLessThan25"/>
		</th>
		<td>
			<acme:print value="${totalLogLessThan25}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogsLessBetween25And50"/>
		</th>
		<td>
			<acme:print value="${totalLogLessBetween25And50}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogsLessBetween50And75"/>
		</th>
		<td>
			<acme:print value="${totalLogLessBetween50And75}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.totalLogsAbove75"/>
		</th>
		<td>
			<acme:print value="${totalLogAbove75}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.averageBudgetContracts"/>
		</th>
		<td>
			<acme:print value="${averageBudgetContracts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.deviationBudgetContracts"/>
		</th>
		<td>
			<acme:print value="${deviationBudgetContracts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.minimunBudgetContracts"/>
		</th>
		<td>
			<acme:print value="${minimumBudgetContracts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.clientDashboard.form.label.maximumBudgetContracts"/>
		</th>
		<td>
			<acme:print value="${maximumBudgetContracts}"/>
		</td>
	</tr>

</table>
<acme:return/>