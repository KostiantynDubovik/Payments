<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title><fmt:message key="admin.label.manage_bills"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/style.css"/>
    <c:set value="${requestScope.bills_list}" var="bills"/>
</head>
<body>
<my:header language="${language}"/>
<table class="body_space">
    <tr>
        <td class="sidebar">
            <div align="center"><my:mainPageImageLink language="${language}"/></div>
        </td>
        <td class="main_space">
            <table class="nav_frame" align="center">
                <tr>
                    <td>
                        <fmt:message key="payments.message.order_by"/><c:out value="     "/>
                        <a href="${pageContext.request.contextPath}/admin/bills?page=${requestScope.currentPage}&order_by=balance">
                            <fmt:message key="bill.message.order_by_balance"/><c:out value="  "/>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/bills?page=${requestScope.currentPage}&order_by=number">
                            <fmt:message key="payments.message.order_by_number"/><c:out value="   "/>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/bills?page=${requestScope.currentPage}&order_by=name">
                            <fmt:message key="bill.message.order_by_name"/><c:out value="    "/>
                        </a>

                    </td>
                </tr>
            </table>
            <table class="credentials">
                <tr>
                    <td>
                        <fmt:message key="bill.message.bills"/>
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${bills != null}">
                        <c:forEach begin="0" end="19" items="${bills}" var="incoming_payment">
                            <tr>
                                <td>
                                    <a href="/admin/bills/bill?number=${incoming_payment.billNumber}">
                                        <c:out value="${incoming_payment.billNumber} - ${incoming_payment.billName} - ${incoming_payment.balance}$"/>
                                    </a>
                                    <c:if test="${incoming_payment.needToUnblock}">
                                        <span style="color: red; ">(!)</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td>
                                <fmt:message key="nothing_to_show"/>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </table>
            <my:pagination language="${language}" address="/admin/bills" list="${bills}"/>
        </td>
    </tr>
</table>
</body>
</html>