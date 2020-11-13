<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ attribute name="entity" required="true" %>
<%@ attribute name="placeholder" required="true" %>
<%@ attribute name="type"%>

<c:set var="type" value="${(empty type ? 'text' : type)}" />
<c:set var="placeholderLocalized"><fmt:message key = "${placeholder}" /></c:set>

    <div class="col-sm-3 my-1">
        <label class="sr-only" for="${entity}Input">${placeholderLocalized}</label>
        <c:if test="${type eq 'email'}">
         <div class="input-group">
            <div class="input-group-prepend">
                <div class="input-group-text">@</div>
            </div>
        </c:if>
            <input type="${type}" name="${entity}"
                   class="form-control form-control"
                   id="${entity}Input"
                   placeholder="${placeholderLocalized}">
        <c:if test="${type eq 'email'}">
         </div>
        </c:if>
    </div>