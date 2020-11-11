<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ attribute name="message" required="true" %>
<%@ attribute name="msgClass" required="false" %>

<c:if test="${not empty message}" >
    <%-- TODO add i18n --%>
    <div class="alert alert-${(empty msgClass) ? 'light' : msgClass}" role="alert">
        <c:out value="${message}" />
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>