<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ attribute name="entity" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="placeholder" required="true" %>
<%@ attribute name="help" required="true" %>
<%@ attribute name="type"%>
<%@ attribute name="min"%>
<%@ attribute name="max"%>
<%@ attribute name="req"%>


<c:set var="type" value="${(empty type ? 'text' : type)}" />
<c:set var="placeholderLocalized"><fmt:message key = "${placeholder}" /></c:set>

<div class="form-group">
    <label for="${entity}Input"><fmt:message key = "${label}" /></label>
    <c:if test="${type eq email}">
    <div class="input-group">
        <div class="input-group-prepend">
            <div class="input-group-text" id="btnGroupAddon">@</div>
        </div>
    </c:if>
        <input type="${type}"
               name="${entity}"
               class="form-control"
               id="${entity}Input"
               aria-describedby="${entity}Help"
               placeholder="${placeholderLocalized}"
               min="${min}"
               max="${max}"
               ${req}
        >
    <c:if test="${type eq email}">
    </div>
    </c:if>
    <small id="${entity}Help" class="form-text text-muted"><fmt:message key = "${help}" /></small>
</div>