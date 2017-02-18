<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="language" required="true" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>
<a href="/user" title="<fmt:message key="main.label.title"/>">
    <img src="${pageContext.request.contextPath}/resources/pics/cash.png" width="121" height="114"/>
</a>