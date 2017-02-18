<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="language" required="true" %>
<%@ attribute name="list" required="true" %>
<%@ attribute name="address" required="true" %>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>

<table class="nav_frame">
    <tr>
        <td>
            <c:if test="${list != null}">
                <c:if test="${requestScope.currentPage != 1}">
                    <a href="${pageContext.request.contextPath}${address}?page=${requestScope.currentPage - 1}&order_by=${requestScope.order_by}"><-<fmt:message key="previous"/></a>
                </c:if>
                <c:forEach begin="1" end="${requestScope.countOfPages}" var="i">
                    <c:choose>
                        <c:when test="${requestScope.currentPage eq i}">${i}</c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}${address}?page=${i}&order_by=${requestScope.order_by}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:if test="${requestScope.currentPage < requestScope.countOfPages}">
                    <a href="${pageContext.request.contextPath}${address}?page=${requestScope.currentPage + 1}&order_by=${requestScope.order_by}"><fmt:message key="next"/>-></a>
                </c:if>
            </c:if>
        </td>
    </tr>
</table>