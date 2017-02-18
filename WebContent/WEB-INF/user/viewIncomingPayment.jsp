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
        <fmt:message key="payments.message.incoming"/>
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
        <td class="main_space" align="center">
            <c:set var="incoming_payment" value="${requestScope.incoming_payment}" scope="page"/>
            <c:if test="${incoming_payment == null}">
                <fmt:message key="nothing_to_show"/>
            </c:if>
            <c:if test="${incoming_payment != null}">
                <table>
                    <tr>
                        <td colspan="2" align="center">
                            <fmt:message key="payments.message.incoming"/>
                        </td>
                    </tr>
                    <c:choose>
                        <c:when test="${incoming_payment != null}">
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.bill_from"/>
                                </td>
                                <td>
                                    <c:out value="${incoming_payment.paymentFromBill}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.to_bill"/>
                                </td>
                                <td>
                                    <a href="/user/bills/bill?number=${incoming_payment.getPaymentToBill().billNumber}">
                                        <c:out value="${incoming_payment.getPaymentToBill().billNumber}"/>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.sum"/>
                                </td>
                                <td>
                                    <c:out value="${incoming_payment.sumOfPayment}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.commission"/>
                                </td>
                                <td>
                                    <c:out value="${incoming_payment.commission}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.date"/>
                                </td>
                                <td>
                                    <c:out value="${incoming_payment.dateOfPayment}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.comment"/>
                                </td>
                                <td>
                                    <c:out value="${incoming_payment.comment}"/>
                                </td>
                            </tr>
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
            </c:if>
        </td>
    </tr>
</table>
</body>
</html>