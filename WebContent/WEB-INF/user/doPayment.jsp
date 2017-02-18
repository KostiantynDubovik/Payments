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
        <fmt:message key="login.label.title"/>
    </title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/style.css"/>

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery.js"></script>
    <script type="text/javascript">
        function viewCommission(whereFromReadNumber, commissionView) {
            $(whereFromReadNumber).keyup(function () {
                var commission = $(this).val() *0.04;
                if (commission < 5) {
                    $(commissionView).text('5');
                } else if (commission > 1000) {
                    $(commissionView).text('1000');
                } else {
                    $(commissionView).text('' + commission);
                }
            });
        }
        $(document).ready(function () {
            var whereFromReadNumber = '#sum';
            var commissionView = '#commissionView';
            viewCommission(whereFromReadNumber, commissionView);
        });
    </script>
</head>
<body>
<my:header language="${language}"/>

<table class="body_space">
    <tr>
        <td class="sidebar">
            <div align="center"><my:mainPageImageLink language="${language}"/></div>
        </td>
        <td class="main_space">
            <form method="post" action="/user/pay">
                <table class="credentials">
                    <tr>
                        <td align="right">
                            <label for="bill_from_number">
                                <fmt:message key="payment.message.choose_bill"/>:
                            </label>
                        </td>
                        <td>
                            <select id="bill_from_number" name="bill_from_number">
                                <c:forEach var="incoming_payment" items="${sessionScope.user.bills}">
                                    <option value="${incoming_payment.billNumber}" <c:if test="${incoming_payment.blocked}">disabled</c:if>> ${incoming_payment.billNumber} ${incoming_payment.billName} ${incoming_payment.balance}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label for="bill_to_number">
                                <fmt:message key="payment.message.input_number"/>:
                            </label>
                        </td>
                        <td>
                            <input id="bill_to_number" name="bill_to_number" required type="number" minlength="16" maxlength="16"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label for="sum">
                                <fmt:message key="payment.message.input_sum"/>:
                            </label>
                        </td>
                        <td>
                            <input id="sum" name="sum" required type="number" minlength="1" maxlength="16" min="1"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <fmt:message key="label.commission"/>:
                        </td>
                        <td>
                            <p><span id="commissionView"></span></p>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label for="comment">
                                <fmt:message key="mail.label.comment"/>:
                            </label>
                        </td>
                        <td>
                            <input id="comment" name="comment" type="text" maxlength="40"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2">
                            <fmt:message key="button.accept" var="buttonValue"/>
                            <input type="submit" name="submit" value="${buttonValue}">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>


</body>
</html>