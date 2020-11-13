<%@ tag pageEncoding="UTF-8" %>
<%--
  -- Selector of interface/db languages, shown as bootStrap dropdown menu
  -- causes Locale settings to be updated on server side
  -- and then the refreshed paged is shown up in a newly selected language
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag import="com.epam.dmivapi.service.impl.command.Command" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>

    <input type="hidden" name="${ContextParam.CURRENT_LOCALE}" id="newLocale" form="${ContextParam.MAIN_PAGE_FORM}" >
    <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <c:out value="${sessionScope[ContextParam.CURRENT_LOCALE]}" />
        </a>
        <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown" id="${Command.SWITCH_LOCALE}">
            <c:forEach var="localeItem" items="${applicationScope[ContextParam.LOCALES]}" varStatus="status">
                    <li class="dropdown-item"
                        data-action="${Command.SWITCH_LOCALE.systemName}"
                        data-locale="${localeItem}">
                            ${localeItem}
                    </li>
            </c:forEach>
        </ul>
    </li>

<script>
    $(document).ready(function(){
        $('#${Command.SWITCH_LOCALE} li').click(function()
        {
            <%--$('#${ContextParam.COMMAND}').val($(this).data('action'));--%>
            $('#${ContextParam.MAIN_PAGE_FORM}').attr('action', $(this).data('action'));
            $('#newLocale').val($(this).data('locale'));
            $('#${ContextParam.MAIN_PAGE_FORM}').submit();
        });
    });
</script>