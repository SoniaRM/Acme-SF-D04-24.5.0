<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.objective.list.label.instantiation-moment" path="instantiationMoment"/>
	<acme:list-column code="authenticated.objective.list.label.title" path="title"/>
	<acme:list-column code="authenticated.objective.list.label.description" path="description"/>
	<acme:list-column code="authenticated.objective.list.label.priority" path="priority"/>
	<acme:list-column code="authenticated.objective.list.label.status" path="status"/>
	<acme:list-column code="authenticated.objective.list.label.duration-start" path="durationStart"/>
	<acme:list-column code="authenticated.objective.list.label.duration-end" path="durationEnd"/>
	<acme:list-column code="authenticated.objective.list.label.link" path="link"/>
	
</acme:list>