<%@ tag pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/directive/taglibs.jspf" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>

<%@attribute name="user" type="com.epam.dmivapi.model.User" %>

<input type="hidden" name="${ContextParam.USER_ID_TO_PROCESS}" value="${param[ContextParam.USER_ID_TO_PROCESS]}">

<c:set var="login"
       value="${(empty param[ContextParam.USR_LOGIN]) ? sessionScope[ContextParam.CURRENT_USER].email : param[ContextParam.USR_LOGIN]}" />
<c:set var="fname"
       value="${(empty param[ContextParam.USR_FIRST_NAME]) ? sessionScope[ContextParam.CURRENT_USER].firstName : param[ContextParam.USR_FIRST_NAME]}" />
<c:set var="lname"
       value="${(empty param[ContextParam.USR_LAST_NAME]) ? sessionScope[ContextParam.CURRENT_USER].lastName : param[ContextParam.USR_LAST_NAME]}" />


<fieldset>
    <legend><fmt:message key="user_info_tag.legend.userInfo"/>:</legend>
<div class="form-group row no-gutters">
    <label for="staticLoginEmail" class="col-sm-auto col-form-label">
        <fmt:message key="list_users_jsp.user.login_email"/>:&nbsp;&nbsp;
    </label>
    <div class="col-sm-auto">
        <input name="${ContextParam.USR_LOGIN}" type="text" readonly class="form-control-plaintext" id="staticLoginEmail"
               value="${login}">
    </div>
    <label for="staticFirstName" class="col-sm-auto col-form-label">
        <fmt:message key="list_users_jsp.user.first_name"/>:&nbsp;&nbsp;
    </label>
    <div class="col-sm-auto">
        <input name="${ContextParam.USR_FIRST_NAME}" type="text" readonly class="form-control-plaintext" id="staticFirstName"
               value="${fname}">
    </div>
    <label for="staticLastName" class="col-sm-auto col-form-label">
        <fmt:message key="list_users_jsp.user.last_name"/>:&nbsp;&nbsp;
    </label>
    <div class="col-sm-auto">
        <input name="${ContextParam.USR_LAST_NAME}" type="text" readonly class="form-control-plaintext" id="staticLastName"
               value="${lname}">
    </div>
</div>
</fieldset>