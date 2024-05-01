<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.project.form.label.code" path="code"/>	
	<acme:input-textbox code="any.project.form.label.title" path="title"/>
	<acme:input-textbox code="any.project.form.label.abstract-project" path="abstractProject"/>
	<acme:input-checkbox code="any.project.form.label.indication" path="indication"/>
	<acme:input-integer code="any.project.form.label.cost" path="cost"/>
	<acme:input-url code="any.project.form.label.link" path="link"/>
</acme:form>
