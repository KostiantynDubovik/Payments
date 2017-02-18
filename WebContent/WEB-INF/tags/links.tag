<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ attribute name="language" required="true" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>
<table class="links">
    <tr>
        <td class="rightcol">
            <my:greating language="${language}"/>
        </td>
    </tr>
    <c:choose>
        <c:when test="${sessionScope.user_role eq applicationScope.guest_role}">

            <tr>
                <td class="rightcol">
                    <a href="/login">
                        <span class="links">
                            <fmt:message key="login.label.title"/>
                        </span>
                    </a>
                </td>
            </tr>
            <tr>
                <td class="rightcol">
                    <a href="/register" class="links">
                        <span class="links">
                            <fmt:message key="register.label.title"/>
                        </span>
                    </a>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <c:if test="${applicationScope.admin_role eq sessionScope.user_role}">
                <tr>
                    <td class="rightcol">
                        <a href="/admin">
                            <span class="links">
                                <fmt:message key="admin.label.title"/>
                            </span>
                        </a>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td class="rightcol">
                    <a href="/user">
                            <span class="links">
                                <fmt:message key="user.account.label.title"/>
                            </span>
                    </a>
                </td>
            </tr>
            <tr>
                <td class="rightcol">
                    <a href="/logout">
                        <span class="links">
                            <fmt:message key="logout.label.title"/>
                        </span>
                    </a>
                </td>
            </tr>
        </c:otherwise>
    </c:choose>
</table>