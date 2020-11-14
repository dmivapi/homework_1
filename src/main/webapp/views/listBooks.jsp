<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="/WEB-INF/jspf/header.jspf"%>
<%--requestScope[ContextParam.BK_TITLE] = ${requestScope[ContextParam.BK_TITLE]}--%>
<%--pageContext.request.getParameter(ContextParam.BK_TITLE) = ${pageContext.request.getParameter(BK_TITLE)}--%>

   <%-- This parameter is used to return to this page after certain commands
     -- and therefore should be present and properly initializated in the body of every page
   --%>
   <input type="hidden"
          name="${ContextParam.SELF_COMMAND}"
          value="${Command.LIST_BOOKS.systemName}"
          id="${ContextParam.SELF_COMMAND}">
   <div class="row">
        <div class="col-sm-12" style="margin: auto">
            <h:book-search-criteria />
              <c:if test="${not empty requestScope[ContextParam.BS_BOOKS]}" >
                <table class="table table-striped" id="list_books_table">
                    <thead>
                    <tr>
                        <th scope="col"><fmt:message key="general_number_column_name"/></th>
                        <th scope="col"><fmt:message key="list_books_jsp.book.title"/></th>
                        <th scope="col"><fmt:message key="list_books_jsp.book.authors"/></th>
                        <th scope="col"><fmt:message key="list_books_jsp.book.genre"/></th>
                        <th scope="col"><fmt:message key="list_books_jsp.book.publisher"/></th>
                        <th scope="col"><fmt:message key="list_books_jsp.book.year"/></th>
                        <c:if test="<%= ContextParam.getCurrentUserRole(session) != null && ContextParam.getCurrentUserRole(session) != Role.GUEST %>">
                           <th scope="col"><fmt:message key="list_books_jsp.select"/></th>
                        </c:if>
                    </tr>
                    </thead>

                    <c:forEach var="book" items="${requestScope[ContextParam.BS_BOOKS]}" varStatus="status">
                        <tr>
                            <th scope="row"><h:pagination-counter rawNumber="${status.count}" /></th>
                            <td>${book.title}</td>
                            <td>${book.authors}</td>
                            <td>${book.genre}</td>
                            <td>${book.publisher}</td>
                            <td>${book.year}</td>
                            <c:if test="<%= ContextParam.getCurrentUserRole(session) != null && ContextParam.getCurrentUserRole(session) != Role.GUEST %>">
                               <td><input type="checkbox" name="${ContextParam.PUBLICATIONS_IDS_TO_PROCESS}" value="${book.id}"/></td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
                <h:pagination-status-actions baseLink="${Command.LIST_BOOKS.path}" message="" />
                <c:if test="<%= ContextParam.getCurrentUserRole(session) != null && ContextParam.getCurrentUserRole(session) == Role.READER %>">
                     <h:button-submit buttonKey="list_books_jsp.button.loan" buttonAction="${Command.LOAN_NEW.systemName}" />
                </c:if>
              </c:if>
            <c:if test="<%= ContextParam.getCurrentUserRole(session) != null && ContextParam.getCurrentUserRole(session) == Role.ADMIN %>">
                <h:button-submit buttonKey="list_books_jsp.button.create" buttonAction="${Command.ENTER_BOOK_INFO.systemName}" />
                <h:button-submit buttonKey="list_books_jsp.button.remove" buttonAction="${Command.BOOK_REMOVE.systemName}" />
            </c:if>
        </div>
    </div>
<%@ include file="../WEB-INF/jspf/footer.jspf"%>