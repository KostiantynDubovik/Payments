<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title><fmt:message key="login.message.success"/></title>
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
            <table class="nav_frame" align="center">
                <tr>
                    <td>
                        <fmt:message key="login.message.blocked"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="logout">
                                    <span class="links">
                                        <fmt:message key="logout.label.title"/>
                                    </span>
                        </a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
