<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag import="com.epam.dmivapi.dto.Role" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>
<%@ tag import="com.epam.dmivapi.service.impl.command.Command" %>

<%@ attribute name="role" required="true" %>

<c:set var="aclasses" value="class=\"list-group-item list-group-item-action bg-light lsa-panel-link\"" />

<c:choose>
    <c:when test = "<%= ContextParam.getCurrentUserRole(session) == Role.ADMIN %>">
        <a href="${Command.LIST_USERS_READERS_FOR_ADMIN.systemName}" ${aclasses}><fmt:message key="left_side_actions_panel_tag.usersBlocking"/></a>
        <a href="${Command.ENTER_USER_INFO.systemName}" ${aclasses}><fmt:message key="left_side_actions_panel_tag.addLibrarian"/></a>
        <a href="${Command.LIST_USERS_LIBRARIANS.systemName}" ${aclasses}><fmt:message key="left_side_actions_panel_tag.viewRemoveLibrarians"/></a>
        <a href="${Command.LIST_BOOKS.systemName}" ${aclasses}><fmt:message key="left_side_actions_panel_tag.books"/></a>
    </c:when>

    <c:when test = "<%= ContextParam.getCurrentUserRole(session) == Role.LIBRARIAN%>">
        <a href="${Command.LIST_USERS_READERS_FOR_LIBRARIAN.systemName}" ${aclasses}><fmt:message key="left_side_actions_panel_tag.readers"/></a>
        <a href="${Command.LIST_LOANS_OF_ALL.systemName}" ${aclasses}><fmt:message key="left_side_actions_panel_tag.bookLoans"/></a>
    </c:when>

    <c:when test = "<%= ContextParam.getCurrentUserRole(session) == Role.READER %>">
        <a href="${Command.LIST_BOOKS.systemName}" ${aclasses}><fmt:message key="left_side_actions_panel_tag.books"/></a>
        <a href="${Command.LIST_LOANS_OF_SELF.systemName}" ${aclasses}><fmt:message key="left_side_actions_panel_tag.myLoans"/></a>
    </c:when>

    <c:when test = "<%= ContextParam.getCurrentUserRole(session) == Role.GUEST %>">
        <a href="${Command.LIST_BOOKS.systemName}"  ${aclasses} ><fmt:message key="left_side_actions_panel_tag.books"/></a>
    </c:when>

    <c:otherwise>
        <a href="${Command.LIST_BOOKS.systemName}" ${aclasses}><fmt:message key="left_side_actions_panel_tag.books"/></a>
    </c:otherwise>
</c:choose>

<script>
    $(document).ready(function(){
        $('.lsa-panel-link').click(function(event)
        {
            event.preventDefault();
            $('#${ContextParam.MAIN_PAGE_FORM}').attr('action', $(this).attr('href'));
            $('#${ContextParam.PGN_CURRENT_PAGE}').val(1);
            $('#${ContextParam.MAIN_PAGE_FORM}').submit();
        });
    });
</script>