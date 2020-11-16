<%@ page import="java.time.LocalDate" %>
<%@ page import="com.epam.dmivapi.dto.LoanDtoViewAll" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/header.jspf"%>
    <div class="row">
        <div class="col-sm-10" style="margin: auto";>
            <%-- This parameter is used to return to this page after certain commands
              -- and therefore should be present and properly initializated in the body of every page
            --%>
            <input type="hidden"
                   name="${ContextParam.SELF_COMMAND}"
                   value="${Command.LIST_LOANS_OF_ALL.systemName}"
                   id="${ContextParam.SELF_COMMAND}">

                <c:if test="<%= ContextParam.getCurrentUserRole(session) != null && ContextParam.getCurrentUserRole(session) == Role.READER %>">
                    <input type="hidden" name="${ContextParam.COMMAND}" value="${Command.LOAN_REMOVE}"/>
                </c:if>

                <table class="table table-striped" id="list_books_table">
                    <thead>
                    <tr>
                        <th scope="col"><fmt:message key="list_books_jsp.book.title"/></th>
                        <th scope="col"><fmt:message key="list_books_jsp.book.authors"/></th>
                        <th scope="col"><fmt:message key="list_books_jsp.book.reader"/></th>
                        <th scope="col"><fmt:message key="list_loans_jsp.status"/></th>
                        <th scope="col"><fmt:message key="list_users_librarians_jsp.label"/></th>
                    </tr>
                    </thead>
                    <c:forEach var="loan" items="${requestScope[ContextParam.LS_LOANS]}" varStatus="status">
                        <tr>
                            <th scope="row">(${loan.libCode}) ${loan.bookTitle}</th>
                            <td>${loan.bookAuthors}</td>
                            <td>${loan.lastName} ${loan.firstName} (${loan.email})</td>
                            <td> <la:getLoanStatusBadge loanStatus="${loan.status}"
                                                        readingRoom="${loan.readingRoom}"
                                                        dueDate="${loan.dueDate}"
                                                        price="${loan.price}" />
                            </td>
                            <td>
                                <h:action-loan-buttons-from-loan-status loanStatus="${loan.status}" loanId="${loan.id}" userId="${loan.userId}" loanBlocked="${loan.blocked}" />
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <h:pagination-status-actions baseLink="${Command.LIST_LOANS_OF_ALL.systemName}" message="" />
        </div>
    </div>
<%@ include file="../WEB-INF/jspf/footer.jspf"%>