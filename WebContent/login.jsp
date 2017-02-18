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
            <table class="credentials">
                <tr>
                    <td>
                        <my:writeError language="${language}" message="not_exist_user_or_wrong_pass"/>
                        <my:writeError language="${language}" message="invalid_login"/>
                        <form method="post" action="login">
                            <tr>
                                <td align="right">
                                    <label for="login">
                                        <fmt:message key="login.label.username"/>:
                                    </label>
                                </td>
                                <td>
                                    <input required type="text" id="login" name="login">
                                </td>
                            </tr>
                            <my:writeError language="${language}" message="invalid_password"/>
                            <tr>
                                <td align="right">
                                    <label for="password">
                                        <fmt:message key="login.label.password"/>:
                                    </label>
                                </td>
                                <td>
                                    <input required type="password" id="password" name="password">
                                </td>
                            </tr>
                            <my:writeError language="${language}" message="invalid_captcha"/>
                            <tr>
                                <td align="right">
                                    <label for="jcaptcha">
                                        <fmt:message key="label.jcaptcha"/>:
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <img src="retrievedCaptcha-image.jpg?s=<rndm:generateRndm/>" id="retrievedCaptcha" onclick="refresh()">
                                    <script>
                                        function refresh() {
                                            document.getElementById("retrievedCaptcha").src = 'retrievedCaptcha-image.jpg?' + (new Date().getTime());
                                        }
                                    </script>
                                </td>
                                <td>
                                    <input required type="text" id="jcaptcha" name="jcaptcha" value=""/>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2">
                                    <fmt:message key="login.button.submit" var="buttonValue"/>
                                    <input type="submit" name="submit" value="${buttonValue}">
                                </td>
                            </tr>
                        </form>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>