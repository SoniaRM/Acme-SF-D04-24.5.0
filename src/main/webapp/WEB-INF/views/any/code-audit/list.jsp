<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.code-audit.list.label.code" path="code" width="10%"/>
	<acme:list-column code="any.code-audit.list.label.execution" path="execution" width="10%"/>
	<acme:list-column code="any.code-audit.list.label.type" path="type" width="10%"/>
	<acme:list-column code="any.code-audit.list.label.optionalLink" path="optionalLink" width="10%"/>
</acme:list>
