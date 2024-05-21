<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="client.progress-log.form.label.record-id" path="recordId"/>
	<acme:input-double code="client.progress-log.form.label.completeness" path="completeness"/>	
	<acme:input-textarea code="client.progress-log.form.label.comment" path="comment"/>	
	<acme:input-moment code="client.progress-log.form.label.registration-moment" path="registrationMoment"/>	
	<acme:input-textarea code="client.progress-log.form.label.responsible-person" path="responsiblePerson"/>	
	<acme:input-select code="client.progress-log.form.label.contract" path="contract" choices="${contracts}"/>
		
		
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="client.progress-log.form.button.update" action="/client/progress-log/update"/>
			<acme:submit code="client.progress-log.form.button.delete" action="/client/progress-log/delete"/>
			<acme:submit code="client.progress-log.form.button.publish" action="/client/progress-log/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="client.progress-log.form.button.create" action="/client/progress-log/create"/>
		</jstl:when>			
	</jstl:choose>
</acme:form>