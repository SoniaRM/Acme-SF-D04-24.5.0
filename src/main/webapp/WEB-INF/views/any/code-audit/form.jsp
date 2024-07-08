<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="any.code-audit.form.label.code" path="code"/>	
	<acme:input-moment code="any.code-audit.form.label.execution" path="execution" readonly="true" />
	<acme:input-textarea code="any.code-audit.form.label.type" path="type"/>
	<acme:input-url code="any.code-audit.form.label.optionalLink" path="optionalLink"/>
</acme:form>