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
        <fmt:message key="bill.message.bills"/>
    </title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/style.css"/>
    <c:set value="${requestScope.bill}" var="incoming_payment"/>
</head>
<body>
<my:header language="${language}"/>

<table class="body_space">
    <tr>
        <td class="sidebar">
            <div align="center"><my:mainPageImageLink language="${language}"/></div>
        </td>
        <td class="main_space">
            <table class="credentials">
                <c:choose>
                    <c:when test="${incoming_payment != null}">
                        <tr>
                            <td>
                                <fmt:message key="bill.message.bills"/>
                            </td>
                            <td>
                                <c:out value="${incoming_payment.billNumber}"/>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <fmt:message key="bill.message.order_by_name"/>
                            </td>
                            <td>
                                <c:out value="${incoming_payment.billName}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <fmt:message key="bill.message.order_by_balance"/>
                            </td>
                            <td>
                                <c:out value="${incoming_payment.balance}"/>
                            </td>
                        </tr>
                        <c:if test="${!incoming_payment.blocked}">
                            <form action="/admin/bills/bill" method="post">
                                <tr>
                                    <td>
                                        <button name="number" value="${incoming_payment.billNumber}b">
                                            <fmt:message key="bill.button.block_bill"/>
                                        </button>
                                    </td>
                                </tr>
                            </form>
                        </c:if>
                        <c:if test="${incoming_payment.blocked}">
                            <form action="/admin/bills/bill" method="post">
                                <tr>
                                    <td>
                                        <button name="number" value="${incoming_payment.billNumber}u">
                                            <fmt:message key="bill.button.unblock_bill"/>
                                        </button>
                                        <c:if test="${incoming_payment.needToUnblock}">
                                            <span style="color: red; ">(!)</span>
                                        </c:if>
                                    </td>
                                </tr>
                            </form>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="nothing_to_show"/>
                    </c:otherwise>
                </c:choose>
            </table>
        </td>
    </tr>
</table>


</body>
</html>