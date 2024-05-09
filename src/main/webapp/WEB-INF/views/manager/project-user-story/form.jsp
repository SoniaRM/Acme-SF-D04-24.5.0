<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <jstl:if test="${_command == 'create'}">
        <acme:input-select code="manager.project.userStory.form.label.user-stories" path="userStory" choices="${userStories}"/>
    </jstl:if>

    <jstl:if test="${acme:anyOf(_command, 'show|delete')}">
        <acme:input-textbox code="manager.userStory.form.label.title" path="userStory.title" readonly="true"/>
        <acme:input-textarea code="manager.userStory.form.label.description" path="userStory.description" readonly="true"/>
        <acme:input-textbox code="manager.userStory.form.label.estimatedCost" path="userStory.estimatedCost" readonly="true"/>
        <acme:input-textbox code="manager.userStory.form.label.lectureNature" path="userStory.acceptanceCriteria" readonly="true"/>
        <acme:input-textbox code="manager.userStory.form.label.acceptanceCriteria" path="userStory.priority" readonly="true"/>
        <acme:input-textbox code="manager.userStory.form.label.link" path="userStory.link" readonly="true"/>
        
        
    </jstl:if>

    <jstl:choose>
    <jstl:when test="${acme:anyOf(_command, 'show|delete')}">
        <acme:submit code="manager.project.userStory.form.button.delete" action="/manager/project-user-story/delete"/>
    </jstl:when>

    <jstl:when test="${_command == 'create'}">
        <acme:submit code="manager.project.userStory.form.button.create" action="/manager/project-user-story/create?masterId=${masterId}"/>
    </jstl:when>
</jstl:choose>
</acme:form>