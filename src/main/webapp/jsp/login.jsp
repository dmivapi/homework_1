<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf"%>

<div class="row">
    <div class="col-sm-6" style="margin: auto";>
        <%--
          -- We use this parameter, to send server information about what page
          -- needs to be refreshed (e.g. in case of language switching)
        --%>
        <input type="hidden"
               name="${ContextParam.SELF_PAGE}"
               value="${pageContext.request.requestURI}"
               id="${ContextParam.SELF_PAGE}">
        <input type="hidden" name="${ContextParam.COMMAND}" value="${Command.LOGIN}"/>
            <h:bs-input-group entity="${ContextParam.USR_LOGIN}" label="login_jsp.label.login" placeholder="login_jsp.placeholder.login" help="login_jsp.help.login" type="email"/>
            <h:bs-input-group entity="${ContextParam.USR_PASSWORD}" label="login_jsp.label.password" placeholder="login_jsp.placeholder.password" help="login_jsp.help.password" type="password" />
            <h:button-submit buttonKey="actions_list_general_tag.login" />

    </div>
    <div class="col-sm"></div>
</div>

<%@ include file="../WEB-INF/jspf/footer.jspf"%>