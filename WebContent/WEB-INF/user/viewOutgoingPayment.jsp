<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="rndm" uri="/WEB-INF/generateRndm.tld" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt2:setLocale value="${language}"/>
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
        <td class="main_space" align="center">
            <c:set var="outgoing_payment" value="${requestScope.outgoing_payment}"/>
            <c:if test="${outgoing_payment == null}">
                <fmt:message key="nothing_to_show"/>
            </c:if>
            <c:if test="${outgoing_payment != null}">
                <table>
                    <tr>
                        <td colspan="2" align="center">
                            <fmt:message key="payments.message.outgoing"/>
                        </td>
                    </tr>
                    <c:choose>
                        <c:when test="${outgoing_payment != null}">
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.bill_from"/>
                                </td>
                                <td>
                                    <a href="/user/bills/bill?number=${outgoing_payment.getBillWhereFromIsPayment().billNumber}">
                                        <c:out value="${outgoing_payment.getBillWhereFromIsPayment().billNumber}"/>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.to_bill"/>
                                </td>
                                <td>
                                    <c:out value="${outgoing_payment.numberOfBillWherePaymentGoing}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.sum"/>
                                </td>
                                <td>
                                    <c:out value="${outgoing_payment.sumOfPayment}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.commission"/>
                                </td>
                                <td>
                                    <c:out value="${outgoing_payment.commission}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.date"/>
                                </td>
                                <td>
                                    <c:out value="${outgoing_payment.dateOfPayment}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="mail.label.comment"/>
                                </td>
                                <td>
                                    <c:out value="${outgoing_payment.comment}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <fmt:message key="payment.message.status"/>
                                </td>
                                <td>
                                    <fmt:message key="${outgoing_payment.paymentStatus.stringValue}"/>
                                </td>
                            </tr>
                            <my:writeError language="${language}" message="to_long_comment"/>
                            <my:writeError language="${language}" message="invalid_bill_from_number"/>
                            <my:writeError language="${language}" message="invalid_bill_to_number"/>
                            <my:writeError language="${language}" message="user_is_not_owner_of_bill"/>
                            <my:writeError language="${language}" message="bill_is_blocked"/>
                            <c:if test="${outgoing_payment.paymentStatus.id==1}">
                                <form action="/user/outgoing_payment" method="post">
                                    <tr>
                                        <td>
                                            <input type="submit" value="<fmt:message key="button.accept"/>" />
                                            <input type="hidden" value="${outgoing_payment.getBillWhereFromIsPayment().billNumber}" name="from" >
                                            <input type="hidden" value="${outgoing_payment.numberOfBillWherePaymentGoing}" name="to" >
                                        </td>
                                    </tr>
                                </form>
                            </c:if>
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