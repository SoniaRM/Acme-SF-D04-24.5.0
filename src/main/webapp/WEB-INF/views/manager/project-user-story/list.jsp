<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="manager.project.userStory.list.label.title" path="userStory.title"/>
    <acme:list-column code="manager.project.userStory.list.label.description" path="userStory.description"/>
</acme:list>

<jstl:if test="${draftMode == true}">
    <acme:button code="manager.project.userStory.button.create" action="/manager/project-user-story/create?masterId=${masterId}"/>
</jstl:if>