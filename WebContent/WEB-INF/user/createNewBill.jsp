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
</head>
<body>
<my:header language="${language}"/>

<table class="body_space">
    <tr>
        <td class="sidebar">
            <div align="center"><my:mainPageImageLink language="${language}"/></div>
        </td>
        <td class="main_space">
            <form method="post" action="/user/new_bill">
                <table class="credentials">
                    <tr>
                        <td align="right">
                            <label for="bill_name">
                                <fmt:message key="bill.message.input_bill_name"/>:
                            </label>
                        </td>
                        <td>
                            <input type="text" maxlength="45" id="bill_name" name="bill_name">
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