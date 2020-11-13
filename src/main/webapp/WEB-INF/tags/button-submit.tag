<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>

<%@ attribute name="buttonKey" required="true" %>
<%@ attribute name="buttonAction" %>
<%@ attribute name="subClass" %>

<button class="btn ${empty subClass ? 'btn-outline-primary' : subClass}"
        type="submit"
<%--        name="${ContextParam.COMMAND}"--%>
        <c:if test="${not empty buttonAction}" >formaction="${buttonAction}"</c:if> >
    <fmt:message key="${buttonKey}" />
</button>