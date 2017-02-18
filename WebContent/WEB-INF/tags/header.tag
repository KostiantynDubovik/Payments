<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ attribute name="language" required="true" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.payments.i18n.text"/>
<header>
    <table class="text">
        <tr>
            <td>
                <form style="align-self: flex-end">
                    <label for="language"></label>
                    <select id="language" name="language" onchange="submit()">
                        <option value="null"></option>
                        <option value="ru" ${language == 'ru_RU' ? 'selected' : ''}>Russian</option>
                        <option value="en" ${language == 'en_EN' ? 'selected' : ''}>English</option>
                    </select>
                </form>
            </td>
            <td class="rightcol">
                <my:links language="${language}"/>
            </td>
        </tr>
    </table>
</header>