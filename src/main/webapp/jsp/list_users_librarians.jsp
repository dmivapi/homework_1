<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf"%>

<div class="row">
    <div class="col-sm-10" style="margin: auto";>
        <%-- This parameter is used to return to this page after certain commands
          -- and therefore should be present and properly initializated in the body of every page
        --%>
        <input type="hidden"
               name="${ContextParam.SELF_COMMAND}"
               value="${Command.LIST_USERS_LIBRARIANS.systemName}"
               id="${ContextParam.SELF_COMMAND}">
        <input type="hidden" name="${ContextParam.COMMAND}" value="${Command.USER_REMOVE}"/>
            <c:if test="${not empty requestScope[ContextParam.USR_USERS]}" >
                <table class="table table-striped table-hover" id="list_users_table">
                    <thead>
                    <tr>
                        <th scope="col"><fmt:message key="general_number_column_name"/></th>
                        <th scope="col"><fmt:message key="list_users_jsp.user.login_email"/></th>
                        <th scope="col"><fmt:message key="list_users_jsp.user.first_name"/></th>
                        <th scope="col"><fmt:message key="list_users_jsp.user.last_name"/></th>
                        <th scope="col"><fmt:message key="list_users_librarians_jsp.label"/></th>
                    </tr>
                    </thead>

                    <c:forEach var="user" items="${requestScope[ContextParam.USR_USERS]}" varStatus="status">
                        <tr>
                            <th scope="row"><h:pagination-counter rawNumber="${status.count}" /></th>
                            <td>${user.email}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>
                                <button type="submit"
                                        class="btn btn-warning btn-sm"
                                        name="${ContextParam.USER_ID_TO_PROCESS}" value="${user.id}">
                                <fmt:message key="list_users_librarians_jsp.button.remove"/>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <h:pagination-status-actions baseLink="${Command.LIST_USERS_LIBRARIANS.path}" message="" />
            </c:if>
        </form>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf"%>