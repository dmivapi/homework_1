<%@ tag pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/directive/taglibs.jspf" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>
<%@ tag import="com.epam.dmivapi.service.impl.command.Command" %>

<%--
    group of controls for book searching criteria,
    it is used at the top of the main page (under nav-bar)
    and is kept there when displaying search results
--%>
    <div class="form-group row">
        <div class="col-auto my-1">
            <label class="sr-only" for="inputTitle">Название</label>
            <c:set var="placeholderLocalized"><fmt:message key = "${'search_book_criteria_tag.placeholder.title'}" /></c:set>
            <input name="${ContextParam.BK_TITLE}" value="${param[ContextParam.BK_TITLE]}" type="text" class="form-control" id="inputTitle" placeholder="${placeholderLocalized}">
        </div>
        <div class="col-auto my-1">
            <label class="sr-only" for="inputAuthor">Автор</label>
            <c:set var="placeholderLocalized"><fmt:message key = "${'search_book_criteria_tag.placeholder.author'}" /></c:set>
            <input name="${ContextParam.BK_AUTHOR}" value="${param[ContextParam.BK_AUTHOR]}" type="text" class="form-control" id="inputAuthor" placeholder="${placeholderLocalized}">
        </div>

        <div class="col-auto my-1">
            <div class="input-group">
                <div class="input-group-prepend">
                    <label class="input-group-text" for="inputGroupSelectSortBy"><fmt:message key="search_book_criteria_tag.label.sorting" /></label>
                </div>
                <select class="custom-select" id="inputGroupSelectSortBy" name="${ContextParam.BS_ORDER_BY}">
                    <option value = "title"><fmt:message key="search_book_criteria_tag.select.by_title" /></option>
                    <option value = "authors"><fmt:message key="search_book_criteria_tag.select.by_author" /></option>
                    <option value = "publisher"><fmt:message key="search_book_criteria_tag.select.by_publisher" /></option>
                    <option value = "year"><fmt:message key="search_book_criteria_tag.select.by_pub_year" /></option>
                </select>
            </div>
        </div>
        <div class="col-auto my-1">
            <select class="form-control" id="inputSelectSortDir" name="${ContextParam.BS_ORDER_BY_DIRECTION}">
                <option value="asc"><fmt:message key="search_book_criteria_tag.select.ascending" /></option>
                <option value="desc"><fmt:message key="search_book_criteria_tag.select.descending" /></option>
            </select>
        </div>
        <div class="col-auto my-1">
            <h:button-submit buttonKey="search_book_criteria_tag.button.search" buttonAction="${Command.LIST_BOOKS.systemName}" subClass="btn-outline-primary btn-sm"/>
        </div>
    </div>

<script>
    $(document).ready(function(){
        $('#inputGroupSelectSortBy option[value="${param[ContextParam.BS_ORDER_BY]}"]').prop('selected', true);
        $('#inputSelectSortDir option[value="${param[ContextParam.BS_ORDER_BY_DIRECTION]}"]').prop('selected', true);
    });
</script>