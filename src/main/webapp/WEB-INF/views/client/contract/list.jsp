<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="client.contract.list.label.code" path="code"/>
	<acme:list-column code="client.contract.list.label.instantiation-moment" path="instantiationMoment"/>
	<acme:list-column code="client.contract.list.label.customer-name" path="customerName"/>
	<acme:list-column code="client.contract.list.label.budget" path="budget"/>
	<acme:list-column code="client.contract.list.label.project" path="project"/>
	<acme:list-column code="client.contract.list.label.draft-mode" path="draftMode"/>
</acme:list>

<acme:button code="client.contract.list.button.create" action="/client/contract/create"/>