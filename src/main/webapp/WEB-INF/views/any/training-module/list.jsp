<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.training-module.list.label.code" path="code" width="10%"/>
	<acme:list-column code="any.training-module.list.label.creation-moment" path="creationMoment" width="10%"/>
	<acme:list-column code="any.training-module.list.label.details" path="details" width="10%"/>	
	<acme:list-column code="any.training-module.list.label.difficulty-level" path="difficultyLevel" width="10%"/>
	<acme:list-column code="any.training-module.list.label.estimated-total-time" path="estimatedTotalTime" width="10%"/>
	<acme:list-column code="any.training-module.list.label.update-moment" path="updateMoment" width="10%"/>
	<acme:list-column code="any.training-module.list.label.link" path="link" width="10%"/>
	<acme:list-column code="any.training-module.list.label.project" path="project" width="10%"/>
</acme:list>
