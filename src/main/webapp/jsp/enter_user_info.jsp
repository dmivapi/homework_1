<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf"%>

<div class="row">
    <div class="col-sm-6" style="margin: auto";>
        <%-- This parameter is used to return to this page after certain commands
          -- and therefore should be present and properly initializated in the body of every page
        --%>
            <input type="hidden"
               name="${ContextParam.SELF_COMMAND}"
               value="${Command.ENTER_USER_INFO.systemName}"
               id="${ContextParam.SELF_COMMAND}">
            <input type="hidden" name="${ContextParam.COMMAND}" value="${Command.REGISTER}"/>
            <h:bs-input-group entity="${ContextParam.USR_LOGIN}" label="login_jsp.label.login" placeholder="login_jsp.placeholder.login" help="login_jsp.help.login" type="email"/>
            <h:bs-input-group entity="${ContextParam.USR_PASSWORD}" label="login_jsp.label.password" placeholder="login_jsp.placeholder.password" help="login_jsp.help.password" type="password" />
            <h:bs-input-group entity="${ContextParam.USR_FIRST_NAME}" label="register_jsp.label.first_name" placeholder="register_jsp.placeholder.first_name" help="register_jsp.help.first_name" />
            <h:bs-input-group entity="${ContextParam.USR_LAST_NAME}" label="register_jsp.label.last_name" placeholder="register_jsp.placeholder.last_name" help="register_jsp.help.last_name" />
            <h:button-submit buttonKey="actions_list_general_tag.register" />
    </div>
    <div class="col-sm"></div>
</div>

<%@ include file="../WEB-INF/jspf/footer.jspf"%>