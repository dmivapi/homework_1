<%@ tag pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/directive/taglibs.jspf" %>

<%@attribute name="baseLink" required="true" rtexprvalue="true" %>
<%@attribute name="message" required="true" %>
<%@attribute name="msgClass" required="false" %>

<h:operation-status-bar message="${message}"
                        msgClass="${(not empty message) && (empty msgClass) ? 'warning' : ''}"
/>
<h:pagination-bar baseLink="${baseLink}" />