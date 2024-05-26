<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<h2>
    <acme:message code="auditor.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.total-code-audits-static"/>
        </th>
        <td>
            <acme:print value="${totalCodeAuditsStatic}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.total-code-audits-dynamic"/>
        </th>
        <td>
            <acme:print value="${totalCodeAuditsDynamic}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.avg-number-audit-records-per-audits"/>
        </th>
        <td>
            <acme:print value="${avgAuditRecords}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.dev-number-audit-records-per-audits"/>
        </th>
        <td>
            <acme:print value="${devAuditRecords}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.min-number-audit-records-per-audits"/>
        </th>
        <td>
            <acme:print value="${minAuditRecords}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.max-number-audit-records-per-audits"/>
        </th>
        <td>
            <acme:print value="${maxAuditRecords}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.avg-period-per-audit-record"/>
        </th>
        <td>
            <acme:print value="${avgPeriod}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.dev-period-per-audit-record"/>
        </th>
        <td>
            <acme:print value="${devPeriod}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.min-period-per-audit-record"/>
        </th>
        <td>
            <acme:print value="${minPeriod}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:message code="auditor.dashboard.form.label.max-period-per-audit-record"/>
        </th>
        <td>
            <acme:print value="${maxPeriod}"/>
        </td>
    </tr>
  </table>
<acme:return/>