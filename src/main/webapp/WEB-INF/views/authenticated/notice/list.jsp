<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.notice.list.label.instantiation-moment" path="instantiationMoment" width="20%"/>
	<acme:list-column code="authenticated.notice.list.label.title" path="title" width="20%"/>
	<acme:list-column code="authenticated.notice.list.label.author" path="author" width="10%"/>
	<acme:list-column code="authenticated.notice.list.label.message" path="message" width="50%"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="authenticated.notice.list.button.create" action="/authenticated/notice/create"/>
</jstl:if>