<%@ page isErrorPage="true" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf"%>

<%--
  -- We use this parameter, to send server information about what page
  -- needs to be refreshed (e.g. in case of language switching)
--%>
<input type="hidden"
       name="${ContextParam.SELF_PAGE}"
       value="${pageContext.request.requestURI}"
       id="${ContextParam.SELF_PAGE}">

    <div class="row">
        <div class="col-md-12">

                <h3>The error has occurred, please contact your system administrator!</h3>

                <%-- this way we get the error information (error 404)--%>
                <c:set var="code" value="${requestScope['javax.servlet.error.status_code']}"/>
                <c:set var="message" value="${requestScope['javax.servlet.error.message']}"/>

            <c:choose>
                <c:when test="${not empty errorMessage}">
                    <h2>Message: ${errorMessage}</h2>
                </c:when>
                <c:otherwise>
                    <h2>Message: ${message}</h2>
                </c:otherwise>
            </c:choose>

            </div>
        </div>
    </div>

<%@ include file="/WEB-INF/jspf/footer.jspf"%>