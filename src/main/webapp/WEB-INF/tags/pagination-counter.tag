<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>

<%@ attribute name="rawNumber" required="true" %>

<c:set var="cPage"
       value="${(empty param[ContextParam.PGN_CURRENT_PAGE]) ? 1 : param[ContextParam.PGN_CURRENT_PAGE]}" />
<c:out value="${rawNumber+(cPage-1)*requestScope[ContextParam.PGN_RECORDS_PER_PAGE]}" />