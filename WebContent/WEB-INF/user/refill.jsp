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
        <fmt:message key="refill.title"/>
    </title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/style.css"/>
    <style type="text/css">
        #commissionView {
        }

        #sum {
        }
    </style>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery.js"></script>
    <script type="text/javascript">
        function viewCommission(whereFromReadNumber, commissionView, amountToBeEnrollment) {
            $(whereFromReadNumber).keyup(function () {
                var commission = $(this).val() * 0.04;
                if (commission < 5) {
                    $(commissionView).text('5');
                    $(amountToBeEnrollment).text($(this).val() - 5)
                } else if (commission > 1000) {
                    $(commissionView).text('1000');
                    $(amountToBeEnrollment).text($(this).val() - 1000)
                } else {
                    $(amountToBeEnrollment).text($(this).val() - commission)
                    $(commissionView).text(commission);
                }
            });
        }
        $(document).ready(function () {
            var whereFromReadNumber = '#sum';
            var commissionView = '#commissionView';
            var amountToBeEnrollment = '#amountToBeEnrollment';
            viewCommission(whereFromReadNumber, commissionView,amountToBeEnrollment);
        });
    </script>
    <c:set var="bills" value="${sessionScope.user.bills}"/>
</head>
<body>
<my:header language="${language}"/>

<table class="body_space">
    <tr>
        <td class="sidebar">
            <div align="center"><my:mainPageImageLink language="${language}"/></div>
        </td>
        <td class="main_space">
            <c:choose>
                <c:when test="${bills!=null && !bills.isEmpty()}">
                    <form method="post" action="/user/refill">
                        <table class="credentials">
                            <tr>
                                <td align="right">
                                    <label for="bill_to_refill_number">
                                        <fmt:message key="payment.message.choose_bill_for_refill"/>:
                                    </label>
                                </td>
                                <td>
                                    <select id="bill_to_refill_number" name="bill_to_refill_number">
                                        <c:forEach var="incoming_payment" items="${sessionScope.user.bills}">
                                            <option name="bill_to_refill_number" value="${incoming_payment.billNumber}"
                                                    <c:if test="${incoming_payment.blocked}">disabled</c:if>> ${incoming_payment.billNumber} ${incoming_payment.billName}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    <label for="sum">
                                        <fmt:message key="payment.message.input_sum"/>:
                                    </label>
                                </td>
                                <td>
                                    <input id="sum" name="sum" required type="number" minlength="1" maxlength="16"
                                           min="6"/>
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
                                    <fmt:message key="label.amount_to_be_enrollment"/>:
                                </td>

                                <td>
                                    <p><span id="amountToBeEnrollment"></span></p>
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
                </c:when>
                <c:otherwise>
                    <table>
                        <tr>
                            <td>
                                <fmt:message key="message.not_have_bill"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <a href="/user/new_bill"><fmt:message key="bill.message.new_bill"/> </a>
                            </td>
                        </tr>
                    </table>

                </c:otherwise>
            </c:choose>

        </td>
    </tr>
</table>


</body>
</html>