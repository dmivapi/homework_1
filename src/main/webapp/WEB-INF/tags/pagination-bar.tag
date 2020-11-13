<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>

<%@ attribute name="baseLink" required="true" %>

<c:set var="currentPage"
       value="${(empty param[ContextParam.PGN_CURRENT_PAGE]) ? 1 : param[ContextParam.PGN_CURRENT_PAGE]}" />
<c:set var="numOfPages" value="${requestScope[ContextParam.PGN_NUMBER_OF_PAGES]}" />

<input type="hidden"
       name="${ContextParam.PGN_CURRENT_PAGE}"
       id="${ContextParam.PGN_CURRENT_PAGE}"
       value="${currentPage}"
       form="${ContextParam.MAIN_PAGE_FORM}" >
<nav aria-label="page navigation">
    <ul class="pagination justify-content-center">
        <c:if test="${currentPage != 1}">
            <li class="page-item">
                <a class="page-link" href="${currentPage-1}">
                    <fmt:message key = "pagination_bar_tag.Previous" />
                </a>
            </li>
        </c:if>
        <c:forEach var="i" begin="1" end="${numOfPages}">
            <li class="page-item${(currentPage eq i) ? ' active' : ''}">
                <a class="page-link" href="${i}">${i}</a>
            </li>
        </c:forEach>
        <c:if test="${currentPage lt numOfPages}">
            <li class="page-item">
                <a class="page-link" href="${currentPage+1}" >
                    <fmt:message key = "pagination_bar_tag.Next" />
                </a>
            </li>
        </c:if>
    </ul>
</nav>
<script>
    $(document).ready(function(){
        $('.page-link').click(function(e) {
            e.preventDefault();
            $('#${ContextParam.MAIN_PAGE_FORM}').attr('action',
                $('#${ContextParam.SELF_COMMAND}').val()
            );
            $('#${ContextParam.PGN_CURRENT_PAGE}').val($(this).attr('href'));
            $('#${ContextParam.MAIN_PAGE_FORM}').submit();
        });
    });
</script>