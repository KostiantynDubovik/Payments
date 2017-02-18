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
        <fmt:message key="main.label.title"/>
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
            <table class="credentials">
                <tr>
                    <td>
                        <a href="user/pay">
                            <fmt:message key="payments.message.pay"/>
                        </a>
                    </td>
                </tr><tr>
                    <td>
                        <a href="user/bills">
                            <fmt:message key="bill.message.bills"/>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="user/outgoing_payments">
                            <fmt:message key="payments.message.outgoing"/>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="user/incoming_payments">
                            <fmt:message key="payments.message.incoming"/>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="user/refill">
                            <fmt:message key="payments.message.refill"/>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="user/new_bill">
                            <fmt:message key="bill.message.new_bill"/>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="user/all_money">
                            <fmt:message key="bill.message.all_user_balance"/>
                        </a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>