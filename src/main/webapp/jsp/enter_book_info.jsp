<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf"%>

        <%-- This parameter is used to return to this page after certain commands
          -- and therefore should be present and properly initializated in the body of every page
        --%>
        <input type="hidden"
               name="${ContextParam.SELF_COMMAND}"
               value="${Command.ENTER_BOOK_INFO.systemName}"
               id="${ContextParam.SELF_COMMAND}">
            <input type="hidden" name="${ContextParam.COMMAND}" value="${Command.BOOK_NEW}"/>

<div class="form-group row">
    <div class="col-auto my-1">
        <h:bs-input-group entity="${ContextParam.BK_TITLE}" label="enter_book_jsp.label.title" placeholder="enter_book_jsp.placeholder.title" help="enter_book_jsp.help.title"
                          req="required"/>
    </div>
    <div class="col-auto my-1">
        <h:bs-input-group entity="${ContextParam.BK_YEAR}" label="enter_book_jsp.label.year" placeholder="enter_book_jsp.placeholder.year" help="enter_book_jsp.help.year"
                          type="number"
                          min="0"
                          max="2020"
                          req="required"
        />
    </div>
</div>

<div class="form-group row">
    <div class="col-auto my-1">
        <div class="input-group">
            <div class="input-group-prepend">
                <label class="input-group-text" for="inputGroupSelectAuthor"><fmt:message key="enter_book_jsp.label.author" /></label>
            </div>
            <select class="custom-select" id="inputGroupSelectAuthor" name="${ContextParam.BK_AUTHOR}">
                <c:forEach var="author" items="${requestScope[ContextParam.BK_AUTHORS]}" varStatus="status">
                    <option value = "${author.id}">${author.firstName} ${author.lastName}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="col-auto my-1">
        <div class="input-group">
            <div class="input-group-prepend">
                <label class="input-group-text" for="inputGroupSelectPublisher"><fmt:message key="enter_book_jsp.label.publisher" /></label>
            </div>
            <select class="custom-select" id="inputGroupSelectPublisher" name="${ContextParam.BK_PUBLISHER}">
                <c:forEach var="publisher" items="${requestScope[ContextParam.BK_PUBLISHERS]}" varStatus="status">
                    <option value = "${publisher.id}">${publisher.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="col-auto my-1">
        <div class="input-group">
            <div class="input-group-prepend">
                <label class="input-group-text" for="inputGroupSelectGenre"><fmt:message key="enter_book_jsp.label.genre" /></label>
            </div>
            <select class="custom-select" id="inputGroupSelectGenre" name="${ContextParam.BK_GENRE}">
                <c:forEach var="genre" items="${requestScope[ContextParam.BK_GENRES]}" varStatus="status">
                    <option value = "${genre.id}">${genre.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
</div>

<div class="form-group row">
    <div class="col-auto my-1">
        <h:bs-input-group entity="${ContextParam.BK_PRICE}" label="enter_book_jsp.label.price" placeholder="enter_book_jsp.placeholder.price" help="enter_book_jsp.help.price"
                          type="number"
                          min="0"
                          max="50000"
                          req="required"
        />
    </div>
    <div class="col-auto my-1">
        <h:bs-input-group entity="${ContextParam.BK_LIB_CODE_BASE}" label="enter_book_jsp.label.libCode" placeholder="enter_book_jsp.placeholder.libCode" help="enter_book_jsp.help.libCode"
                          req="required"
        />
    </div>
    <div class="col-auto my-1">
        <h:bs-input-group entity="${ContextParam.BK_QUANTITY}" label="enter_book_jsp.label.quantity" placeholder="enter_book_jsp.placeholder.quantity" help="enter_book_jsp.help.quantity"
                          type="number"
                          min="0"
                          max="1000"
                          req="required"
        />
    </div>
</div>
<h:button-submit buttonKey="list_books_jsp.button.create" />

<%@ include file="../WEB-INF/jspf/footer.jspf"%>