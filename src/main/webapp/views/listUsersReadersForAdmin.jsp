<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf"%>

<%-- This parameter is used to return to this page after certain commands
  -- and therefore should be present and properly initializated in the body of every page
--%>
    <input type="hidden"
       name="${ContextParam.SELF_COMMAND}"
       value="${Command.LIST_USERS_READERS_FOR_ADMIN.systemName}"
       id="${ContextParam.SELF_COMMAND}">
    <div class="row">
        <div class="col-sm-10" style="margin: auto";>
                <table class="table table-striped table-hover" id="list_users_table">
                    <thead>
                    <tr>
                        <th scope="col"><fmt:message key="general_number_column_name"/></th>
                        <th scope="col"><fmt:message key="list_users_jsp.user.login_email"/></th>
                        <th scope="col"><fmt:message key="list_users_jsp.user.first_name"/></th>
                        <th scope="col"><fmt:message key="list_users_jsp.user.last_name"/></th>
                        <th scope="col"><fmt:message key="list_users_readers_jsp.blocked"/></th>
                    </tr>
                    </thead>

                    <c:forEach var="user" items="${requestScope[ContextParam.USR_USERS]}" varStatus="status">
                        <tr>
                            <th scope="row"><h:pagination-counter rawNumber="${status.count}" /></th>
                            <td>${user.email}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>
                                <div class="custom-control custom-switch">
                                    <input type="checkbox"
                                           class="custom-control-input"
                                           name="${ContextParam.USER_BLOCK_TO_PROCESS}"
                                           ${user.blocked ? 'checked' : ''}
                                           id="${user.id}"
                                    />
                                    <label class="custom-control-label" for="${user.id}"></label>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <h:pagination-status-actions baseLink="${Command.LIST_USERS_READERS_FOR_ADMIN.path}" message="" />
        </div>
    </div>

<input type="hidden" name="${ContextParam.USER_ID_TO_PROCESS}" id="clickedUserId" form="${ContextParam.MAIN_PAGE_FORM}">
<input type="hidden" name="${ContextParam.BLOCK_OPTION}" id="clickedCheckValue"  form="${ContextParam.MAIN_PAGE_FORM}">
<script>
    $(document).ready(function(){
        $('.custom-control-input').click(function()
        {
            $('#${ContextParam.COMMAND}').val("${Command.USER_BLOCK.systemName}");
            $('#clickedUserId').val($(this).attr('id'));
            $('#clickedCheckValue').val($(this).is(':checked'));
            $('#${ContextParam.MAIN_PAGE_FORM}').submit();
        });
    });
</script>
<%@ include file="../WEB-INF/jspf/footer.jspf"%>