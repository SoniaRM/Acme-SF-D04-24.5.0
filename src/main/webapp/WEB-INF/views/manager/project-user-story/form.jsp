<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <jstl:if test="${_command == 'create'}">
        <acme:input-select code="manager.project.userStory.form.label.projects" path="project" choices="${projects}"/>
        <acme:input-select code="manager.project.userStory.form.label.user-stories" path="userStory" choices="${userStories}"/>
    
    </jstl:if>
    
       <jstl:choose>	
		<jstl:when test="${_command == 'create'}">
    <acme:submit code="manager.project.userStory.form.button.create" action="/manager/project-user-story/create"/>
		</jstl:when> 	
	</jstl:choose>
    
    
       <jstl:if test="${_command == 'delete'}">
        <acme:input-select code="manager.project.userStory.form.label.user-stories" path="userStory" choices="${userStories}"/>

    </jstl:if>
    <jstl:choose>	
		<jstl:when test="${_command == 'delete'}">
		        <input type="hidden" name="userStoryId" value="${userStoryId}"/>
		
			<acme:submit code="manager.projectUserStory.form.button.remove" action="/manager/project-user-story/delete?projectId=${projectId}"/>
		</jstl:when> 	
	</jstl:choose>
</acme:form>