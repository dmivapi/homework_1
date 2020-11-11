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
<h1 class="mt-4">Вы вышли из системы!</h1>
<p>Войдите чтобы получить доступ к заказу книг и просмотра списка книг взятых на абонемент.</p>

<%@ include file="../WEB-INF/jspf/footer.jspf"%>