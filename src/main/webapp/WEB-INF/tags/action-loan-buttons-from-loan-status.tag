<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag import="com.epam.dmivapi.ContextParam" %>
<%@ tag import="com.epam.dmivapi.service.impl.command.Command" %>
<%@ tag import="com.epam.dmivapi.dto.LoanStatus" %>

<%--
    Buttons representing actions that can be performed
    on selected list items
--%>

<%@ attribute name="loanStatus" required="true"
              type="com.epam.dmivapi.dto.LoanStatus"
              rtexprvalue="true"
%>
<%@ attribute name="userId" required="true"
              rtexprvalue="true"
%>
<%@ attribute name="loanBlocked" required="true"
              type="java.lang.Boolean"
              rtexprvalue="true"
%>

<%@ attribute name="loanId" required="true" rtexprvalue="true" %>

<div class="btn-group dropright">
    <c:if test = "<%= loanStatus != LoanStatus.RETURNED %>">
      <button type="button" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          <fmt:message key="list_loans_jsp.button.actions" />
      </button>
      <div class="dropdown-menu">
    </c:if>
        <c:choose>
            <c:when test = "<%= loanStatus == LoanStatus.NEW %>">
            <c:if test = "${not loanBlocked}">
                <a class="dropdown-item" href="#" data-lid="${loanId}" data-action="${Command.LOAN_OUT.systemName}">
                    <fmt:message key="list_loans_jsp.button.lend_out" />
                </a>
            </c:if>
                <a class="dropdown-item" href="#" data-lid="${loanId}" data-action="${Command.LOAN_REMOVE.systemName}">
                    <fmt:message key="list_loans_jsp.button.remove" />
                </a>
            </c:when>
            <c:when test = "<%= loanStatus == LoanStatus.OUT || loanStatus == LoanStatus.OVERDUE %>">
                <a class="dropdown-item" href="#" data-lid="${loanId}" data-action="${Command.LOAN_IN.systemName}">
                    <fmt:message key="list_loans_jsp.button.return" />
                </a>
            </c:when>

            <c:otherwise> </c:otherwise>
        </c:choose>
<c:if test = "<%= loanStatus != LoanStatus.RETURNED %>">
    </div>
</div>
</c:if>

<input type="hidden" name="${ContextParam.LOAN_ID_TO_PROCESS}" id="${ContextParam.LOAN_ID_TO_PROCESS}">
<script>
    $(document).ready(function(){
        $('.dropdown-item').click(function(e)
        {
            alert(${Command.LIST_LOANS_OF_USER} + userId + $(this).data('action'));
            <%--$('#${ContextParam.COMMAND}').val($(this).data('action'));--%>
            $('#${ContextParam.MAIN_PAGE_FORM}').attr('action',
                ${Command.LIST_LOANS_OF_USER} + userId + $(this).data('action')
            );
            $('#${ContextParam.LOAN_ID_TO_PROCESS}').val($(this).data('lid'));
            $('#${ContextParam.MAIN_PAGE_FORM}').submit();
        });
    });
</script>