<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="language" required="true" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>
    <span class="links">
        <fmt:message key="hello"/>,
        <c:choose>
            <c:when test="${sessionScope.user_role eq applicationScope.guest_role}"><fmt:message key="guest"/>!</c:when>
            <c:otherwise>${sessionScope.user.firstName}!</c:otherwise>
        </c:choose>
    </span>