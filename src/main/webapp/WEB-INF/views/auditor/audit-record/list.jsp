<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.audit-record.list.label.code" path="code" width="10%"/>
	<acme:list-column code="auditor.audit-record.list.label.mark" path="mark" width="10%"/>
	<acme:list-column code="auditor.audit-record.list.label.draft-mode" path="draftMode" width="10%"/>
	<acme:list-column code="auditor.audit-record.list.label.final-period" path="finalPeriod" width="10%"/>	
</acme:list>
		
<acme:button code="auditor.audit-record.list.button.create" action="/auditor/audit-record/create"/>