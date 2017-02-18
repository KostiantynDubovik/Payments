<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="language" required="true" %>
<%@ attribute name="message" required="true" %>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>

<c:set var="error_messages" value="${sessionScope.error_messages}"/>
<c:set var="errors" value="${error_messages !=null && !error_messages.isEmpty()}"/>

<c:if test="${errors && error_messages[message]!=null}">
    <tr>
        <td colspan="2" align="center">
            <span style="color: red; "><fmt:message key="${error_messages[message]}"/></span>
        </td>
    </tr>
</c:if>