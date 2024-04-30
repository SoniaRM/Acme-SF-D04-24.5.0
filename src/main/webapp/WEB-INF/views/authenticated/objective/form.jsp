<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="authenticated.objective.form.label.instantiation-moment" path="instantiationMoment"/>	
	<acme:input-textbox code="authenticated.objective.form.label.title" path="title"/>
	<acme:input-textarea code="authenticated.objective.form.label.description" path="description"/>
	<acme:input-textarea code="authenticated.objective.form.label.priority" path="priority"/>
	<acme:input-checkbox code="authenticated.objective.form.label.status" path="status"/>
	<acme:input-moment code="authenticated.objective.form.label.duration-start" path="durationStart"/>
	<acme:input-moment code="authenticated.objective.form.label.duration-end" path="durationEnd"/>
	<acme:input-url code="authenticated.objective.form.label.link" path="link"/>
</acme:form>
