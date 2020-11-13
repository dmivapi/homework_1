<%@ tag pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/directive/taglibs.jspf" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>
<%@ tag import="com.epam.dmivapi.service.impl.command.Command" %>

<form id="login_form" method="post">
    <div class="form-row align-items-right">
        <h:inline-input-group entity="${ContextParam.USR_LOGIN}" placeholder="login_jsp.placeholder.login"  type="email" />
        <h:inline-input-group entity="${ContextParam.USR_PASSWORD}" placeholder="login_jsp.placeholder.password"  type="password" />
        <div class="col-auto my-1">
            <button type="submit" formaction="${Command.LOGIN}" class="btn btn-outline-primary btn-sm"><fmt:message key="actions_list_general_tag.login"/></button>
        </div>
        <div class="col-auto my-1">
            <button type="submit"  formaction="${Command.ENTER_USER_INFO}" class="btn btn-outline-secondary btn-sm"><fmt:message key="actions_list_general_tag.register"/></button>
        </div>
    </div>
</form>