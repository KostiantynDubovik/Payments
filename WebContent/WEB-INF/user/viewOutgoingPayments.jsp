<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="rndm" uri="/WEB-INF/generateRndm.tld" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title>
        <fmt:message key="payments.message.outgoing"/>
    </title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/style.css"/>
</head>
<body>
<my:header language="${language}"/>

<table class="body_space">
    <tr>
        <td class="sidebar">
            <div align="center"><my:mainPageImageLink language="${language}"/></div>
        </td>
        <td class="main_space">
            <c:set var="outgoing_payments_list" value="${requestScope.outgoing_payments}" scope="page"/>
            <c:if test="${outgoing_payments_list == null}">
                <fmt:message key="nothing_to_show"/>
            </c:if>
            <c:if test="${outgoing_payments_list != null}">
                <table class="nav_frame" align="center">
                    <tr>
                        <td>
                            <fmt:message key="payments.message.order_by"/><c:out value="     "/>
                            <a href="${pageContext.request.contextPath}/user/outgoing_payments?page=${requestScope.currentPage}&order_by=number">
                                <fmt:message key="payments.message.order_by_number"/><c:out value="  "/>
                            </a>
                            <c:out value="   "/><fmt:message key="payments.message.order_by_date"/><c:out value=": "/>
                            <a href="${pageContext.request.contextPath}/user/outgoing_payments?page=${requestScope.currentPage}&order_by=date">
                                <fmt:message key="payments.message.newest_to_oldest"/><c:out value="   "/>
                            </a>
                            <a href="${pageContext.request.contextPath}/user/outgoing_payments?page=${requestScope.currentPage}&order_by=date DESC">
                                <fmt:message key="payments.message.oldest_to_newest"/><c:out value="    "/>
                            </a>

                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td>
                            <fmt:message key="payments.message.outgoing"/>
                        </td>
                    </tr>
                    <c:choose>
                        <c:when test="${outgoing_payments_list != null}">
                            <c:forEach begin="0" end="19" items="${outgoing_payments_list}" var="outgoing_payment">
                                <tr>
                                    <td>
                                        <a href="/user/outgoing_payment?id=${outgoing_payment.id}">
                                            <c:out value="${outgoing_payment.getBillWhereFromIsPayment().getBillNumber()} -> ${outgoing_payment.getNumberOfBillWherePaymentGoing()} ${outgoing_payment.sumOfPayment}"/>
                                        </a>
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
                <my:pagination language="${language}" address="/user/outgoing_payments" list="${outgoing_payments_list}"/>
            </c:if>
        </td>
    </tr>
</table>
</body>
</html>