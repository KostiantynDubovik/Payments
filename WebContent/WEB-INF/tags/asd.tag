<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="language" required="true" %>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="com.myShop.i18n.text"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style type="text/css">
        .treeview { padding: 0; clear: both; font-family: Arial, sans-serif; width: 100%; }
        .treeview * { font-size: 100.1%; }
        .treeview ul
        {
            overflow: hidden; width: 100%; margin: 0; padding: 0 0 1.5em 0;
            list-style-type: none;
        }
        .treeview ul ul { overflow: visible; width: auto; margin: 0 0 0 0; padding: 0 0 0 0.75em; }
        /* класс для ul после которых нет li в родительских ветках */
        .treeview ul.l { border-left: 1px solid; margin-left: -1px; }
        .treeview li.cl ul { display: none; }
        .treeview li { margin: 0; padding: 0; }
        .treeview li li { margin: 0 0 0 0.5em; border-left: 1px dotted; padding: 0; }
        .treeview li div { position: relative; height: 1.5em; min-height: 16px; //height: 1.3em; }
        .treeview li li div { border-bottom: 1px dotted; }
        .treeview li p
        {
            position: absolute; z-index: 1; top: 0.8em; //top: 0.65em; left: 1.75em;
            width: 100%; margin: 0; border-bottom: 1px dashed; padding: 0;
        }
        .treeview a { padding: 0.1em 0.2em; white-space: nowrap; //height: 1px; }
        .treeview img.i
        {
            border-right: 2px solid; border-bottom: 0.5em solid;
            margin-bottom: -0.5em; vertical-align: middle;
        }
        .treeview a.sc
        {
            position: absolute; top: 0.06em;
            margin-left: -1em; padding: 0; text-decoration: none;
        }

        /* colors */
        .treeview li p,
        .treeview img.i,
        .treeview .sc
        { background: #456a8e; }
        .treeview ul.l,
        .treeview li p,
        .treeview img.i
        { border-color: #456a8e; }
        .treeview ul li li,
        .treeview ul li li div
        { border-color: #456a8e; }
        .treeview a,
        .treeview a.sc,
        .treeview a.sc:hover
        { color: #edeef0; }
        .treeview a:hover
        { color: #cecfd1; }
    </style>
    <script type="text/javascript">
        function UnHide( eThis ){
            if( eThis.innerHTML.charCodeAt(0) == 9658 ){
                eThis.innerHTML = '&#9660;';
                eThis.parentNode.parentNode.parentNode.className = '';
            }else{
                eThis.innerHTML = '&#9658;';
                eThis.parentNode.parentNode.parentNode.className = 'cl';
            }
            return false;
        }
    </script>
</head>
<body>
<table>


    <div class="treeview">
        <c:forEach items="${applicationScope.categories}" var="category">
            <ul>
                <li>
                <li class="cl">
                    <div>
                        <p>
                            <a href="#" class="sc" onclick="return UnHide(this)">&#9658;</a>
                            <a href="main?category=${category.categoryName}&order_by=product_name">
                                <fmt:message key="categories.${category.categoryName}"/>
                            </a>
                        </p>
                    </div>
                    <c:forEach items="${category.subCategories}" var="subCategory">
                        <ul>
                            <li>
                                <div>
                                    <p>
                                        <a href="main?subcategory=${subCategory.subCategoryName}&order_by=product_name">
                                            <fmt:message key="subcategories.${subCategory.subCategoryName}"/>
                                        </a>
                                    </p>
                                </div>
                            </li>
                        </ul>
                    </c:forEach>
                </li>
            </ul>
        </c:forEach>
    </div>
</table>
</body>
</html>