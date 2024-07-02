<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:list>
	<acme:list-column code="manager.user-story.list.label.title" path="title" width="10%"/>
	<acme:list-column code="manager.user-story.list.label.description" path="description" width="10%"/>
	<acme:list-column code="manager.user-story.list.label.estimated-cost" path="estimatedCost" width="10%"/>
	<acme:list-column code="manager.user-story.list.label.draft-mode" path="draftMode" width="10%"/>
</acme:list>

<jstl:if test="${show}">
    <acme:button code="manager.user-story.list.button.create" action="/manager/user-story/create"/>
    <acme:button code="manager.project.userStory.form.button.create" action="/manager/project-user-story/create?masterId=${masterId}"/>
</jstl:if>

<jstl:if test="${_command == 'list-all'}">
  <acme:button code="manager.user-story.list.button.create" action="/manager/user-story/create"/>
  <acme:button code="manager.project.userStory.form.button.create" action="/manager/project-user-story/create?masterId=${masterId}"/>
</jstl:if>


