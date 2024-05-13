<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <jstl:if test="${_command == 'create'}">
        <acme:input-select code="manager.project.userStory.form.label.projects" path="project" choices="${projects}"/>
        <acme:input-select code="manager.project.userStory.form.label.user-stories" path="userStory" choices="${userStories}"/>
    
    </jstl:if>

    <acme:submit code="manager.project.userStory.form.button.delete" action="/manager/project-user-story/delete"/>

    <acme:submit code="manager.project.userStory.form.button.create" action="/manager/project-user-story/create"/>
</acme:form>