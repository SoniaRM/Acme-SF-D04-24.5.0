<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="developer.training-session.list.label.code" path="code" width="10%"/>
	<acme:list-column code="developer.training-session.list.label.location" path="location" width="10%"/>
	<acme:list-column code="developer.training-session.list.label.instructor" path="instructor" width="10%"/>
	<acme:list-column code="developer.training-session.list.label.draft-mode" path="draftMode" width="10%"/>
	<acme:list-column code="developer.training-session.list.label.training-module" path="trainingModule" width="10%"/>
</acme:list>

<acme:button code="developer.training-session.list.button.create" action="/developer/training-session/create"/>