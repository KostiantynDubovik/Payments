<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="rndm" uri="/WEB-INF/generateRndm.tld" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title><fmt:message key="register.label.title"/></title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/style.css"/>
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
                <form action="register" method="post">
                    <my:writeError language="${language}" message="invalid_login"/>
                    <my:writeError language="${language}" message="exist_login"/>
                    <tr>
                        <td align="right">
                            <label for="login">
                                <fmt:message key="register.label.username"/>:
                            </label>
                        </td>
                        <td>
                            <input required type="text" id="login" name="login"/>
                        </td>
                    </tr>
                    <my:writeError language="${language}" message="invalid_password"/>
                    <tr>
                        <td align="right">
                            <label for="password">
                                <fmt:message key="register.label.password"/>:
                            </label>
                        </td>
                        <td>
                            <input required type="password" id="password" name="password"/>
                        </td>
                    </tr>
                    <my:writeError language="${language}" message="password_not_match"/>
                    <tr>
                        <td align="right">
                            <label for="password_check">
                                <fmt:message key="register.label.password_confirm"/>:
                            </label>
                        </td>
                        <td>
                            <input required type="password" id="password_check" name="password_check"/>
                        </td>
                        <script>
                            document.addEventListener('DOMContentLoaded', function () {
                                var pass1 = document.querySelector('#password'), pass2 = document.querySelector('#password_check');
                                pass1.addEventListener('keyup', function () {
                                    this.value != pass2.value ? pass2.setCustomValidity('<fmt:message
                                key="register.label.password_mismatch"/>') : pass2.setCustomValidity('')
                                });
                                pass2.addEventListener('keyup', function () {
                                    this.value != pass1.value ? this.setCustomValidity('<fmt:message
                         key="register.label.password_mismatch"/>') : this.setCustomValidity('')
                                })
                            })
                        </script>
                    </tr>
                    <my:writeError language="${language}" message="invalid_email"/>
                    <my:writeError language="${language}" message="exist_email"/>
                    <tr>
                        <td align="right">
                            <label for="email">
                                <fmt:message key="register.label.email"/>:
                            </label>
                        </td>
                        <td>
                            <input required type="email" id="email" name="email"/>
                        </td>
                    </tr>
                    <my:writeError language="${language}" message="invalid_firstname"/>
                    <tr>
                        <td align="right">
                            <label for="firstname">
                                <fmt:message key="register.label.firstname"/>:
                            </label>
                        </td>
                        <td>
                            <input required type="text" id="firstname" name="firstname"/>
                        </td>
                    </tr>
                    <my:writeError language="${language}" message="invalid_lastname"/>
                    <tr>
                        <td align="right">
                            <label for="lastname">
                                <fmt:message key="register.label.lastname"/>:
                            </label>
                        </td>
                        <td>
                            <input required type="text" id="lastname" name="lastname"/>
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
                            <fmt:message key="button.submit" var="buttonValue"/>
                            <input type="hidden" name="user_language" value="${language}">
                            <input type="submit" name="submit" value="${buttonValue}">
                        </td>
                    </tr>
                    <c:if test="${sessionScope.error_message != null}">
                        <tr>
                            <td align="center" colspan="2">
                                <fmt:message key="${sessionScope.error_message}"/>
                            </td>
                        </tr>
                    </c:if>
                </form>
            </table>
        </td>
    </tr>
</table>

</body>
</html>