<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="manager.project.form.label.code" path="code"/>
	<acme:input-textbox code="manager.project.form.label.title" path="title"/>
	<acme:input-textbox code="manager.project.form.label.abstract-project" path="abstractProject"/>
	<acme:input-checkbox code="manager.project.form.label.indication" path="indication"/>
	<acme:input-integer code="manager.project.form.label.cost" path="cost"/>
	<acme:input-url code="manager.project.form.label.link" path="link"/>
	
	
		
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="manager.project.form.button.user-stories" action="/manager/user-story/list-mine?masterId=${id}"/>
        </jstl:when>
        
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="manager.project.form.button.update" action="/manager/project/update"/>
			<acme:submit code="manager.project.form.button.delete" action="/manager/project/delete"/>
			<acme:submit code="manager.project.form.button.publish" action="/manager/project/publish"/>
			<acme:button code="manager.project.form.button.user-stories" action="/manager/user-story/list-mine?masterId=${id}"/>		
			<acme:button code="manager.userStory.list.button.remove" action="/manager/project-user-story/delete?projectId=${id}" />
		
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.project.form.button.create" action="/manager/project/create"/>
		</jstl:when>			
	
		
	</jstl:choose>

	
</acme:form>
