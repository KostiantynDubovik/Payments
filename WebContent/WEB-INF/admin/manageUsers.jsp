<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title><fmt:message key="admin.label.manage_user"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/style.css"/>
    <c:set var="users" value="${requestScope.users}"/>
</head>
<body>
<my:header language="${language}"/>
<table class="body_space">
    <tr>
        <td class="sidebar">
            <div align="center"><my:mainPageImageLink language="${language}"/></div>
        </td>
        <td class="main_space">
            <table class="product_frame_all">
                <tr>
                    <td>
                        <c:forEach begin="0" end="19" items="${users}" var="user">
                            <table class="credentials">
                                <tr>
                                    <td>
                                        <fmt:message key="login.label.username"/>
                                    </td>
                                    <td>
                                        <c:out value="${user.login}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="user.account.label.firstname"/>
                                    </td>
                                    <td>
                                        <c:out value="${user.firstName}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="user.account.label.lastname"/>
                                    </td>
                                    <td>
                                        <c:out value="${user.lastName}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="/admin/bills?user_id=${user.id}"><fmt:message key="bill.message.bills"/></a>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <fmt:message key="login.label.title"/>
                                    </td>
                                    <td>
                                        <c:out value="${user.login}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <form action="/admin/users" method="post">
                                            <input type="hidden" name="user_login" value="${user.login}"/>
                                            <c:out value="${user.login}"/>
                                            <c:choose>
                                                <c:when test="${!user.blocked}">
                                                    <input type="hidden" name="action" value="block"/>
                                                    <input type="submit"
                                                           value="<fmt:message key="admin.button.block_user"/>">
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="hidden" name="action" value="unblock"/>
                                                    <input type="submit"
                                                           value="<fmt:message key="admin.button.unblock_user"/>">
                                                </c:otherwise>
                                            </c:choose>
                                        </form>
                                    </td>
                                </tr>
                            </table>
                        </c:forEach>
                        <my:pagination language="${language}" address="/admin/users" list="${users}"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>