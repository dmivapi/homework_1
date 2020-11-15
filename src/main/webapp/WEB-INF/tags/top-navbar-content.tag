<%@ tag pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/directive/taglibs.jspf" %>
<%@ tag import="com.epam.dmivapi.dto.Role" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>
<%@ tag import="com.epam.dmivapi.Path" %>
<%@ tag import="com.epam.dmivapi.service.impl.command.Command" %>

<div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav ml-auto mt-2 mt-lg-0">
        <h:lang-selector />
        <c:choose>
            <c:when test="<%= ContextParam.getCurrentUserRole(session) == Role.GUEST %>">
                <li class="nav-item active">
                    <h:login_register />
                </li>
            </c:when>
            <c:otherwise>
                <li class="nav-item active">
                    <h:logout />
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>