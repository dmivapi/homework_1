<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf"%>

<%-- This parameter is used to return to this page after certain commands
  -- and therefore should be present and properly initializated in the body of every page
--%>
    <input type="hidden"
       name="${ContextParam.SELF_COMMAND}"
       value="${Command.LIST_USERS_READERS_FOR_LIBRARIAN.systemName}"
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
                    </tr>
                    </thead>

                    <c:forEach var="user" items="${requestScope[ContextParam.USR_USERS]}" varStatus="status">
                        <tr class="clickable-row"
                            data-uid="${user.id}" data-login="${user.email}" data-fname="${user.firstName}" data-lname="${user.lastName}"
                            style="cursor: pointer">
                            <th scope="row"><h:pagination-counter rawNumber="${status.count}" /></th>
                            <td>${user.email}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                        </tr>
                    </c:forEach>
                </table>
                <h:pagination-status-actions baseLink="${Command.LIST_USERS_READERS_FOR_LIBRARIAN.path}" message="" />
        </div>
    </div>

<input type="hidden" name="${ContextParam.USR_LOGIN}" id="${ContextParam.USR_LOGIN}">
<input type="hidden" name="${ContextParam.USR_FIRST_NAME}" id="${ContextParam.USR_FIRST_NAME}">
<input type="hidden" name="${ContextParam.USR_LAST_NAME}" id="${ContextParam.USR_LAST_NAME}">
<script>
    $(document).ready(function(){
        $('.clickable-row').click(function()
        {

            $('#${ContextParam.MAIN_PAGE_FORM}').attr(
                'action',
                "${Command.LIST_LOANS_OF_USER.systemName}" + $(this).data('uid')
            );
            $('#${ContextParam.USR_LOGIN}').val($(this).data('login'));
            $('#${ContextParam.USR_FIRST_NAME}').val($(this).data('fname'));
            $('#${ContextParam.USR_LAST_NAME}').val($(this).data('lname'));
            $('#${ContextParam.PGN_CURRENT_PAGE}').val(1);
            $('#${ContextParam.MAIN_PAGE_FORM}').submit();
        });
    });
</script>
<%@ include file="../WEB-INF/jspf/footer.jspf"%>