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
<%--            <div class="error-template">

--%>
                <h3>The error has occurred, please contact your system administrator!</h3>

                <%-- this way we get the error information (error 404)--%>
                <c:set var="code" value="${requestScope['javax.servlet.error.status_code']}"/>
                <c:set var="message" value="${requestScope['javax.servlet.error.message']}"/>

                <%-- this way we get the exception --%>
<%--                <c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>--%>


<%--                <c:if test="${not empty code}">--%>
<%--                    <h2>Error code: ${code}</h2>--%>
<%--                </c:if>--%>
<%----%>
                <c:if test="${not empty message}">
                    <h2>Message: ${message}</h2>
                </c:if>

                <%-- if get this page using forward --%>
                <c:if test="${not empty sessionScope[ContextParam.ERROR_MESSAGE]}">
                    <h3>This is what we know: ${sessionScope[ContextParam.ERROR_MESSAGE]}</h3>
                </c:if>

<%--                <div class="error-details">--%>
<%--                     this way we print exception stack trace--%>
<%--                    <c:if test="${not empty exception}">--%>
<%--                        <hr/>--%>
<%--                        <h3>Stack trace:</h3>--%>
<%--                        <c:forEach var="stackTraceElement" items="${exception.stackTrace}">--%>
<%--                            ${stackTraceElement}--%>
<%--                        </c:forEach>--%>
<%--                    </c:if>--%>
<%--                </div>--%>
            </div>
        </div>
    </div>

<%@ include file="/WEB-INF/jspf/footer.jspf"%>