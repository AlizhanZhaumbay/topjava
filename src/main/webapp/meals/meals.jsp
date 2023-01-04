<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 04.01.2023
  Time: 18:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<body>
<h1>Meals</h1>
<p><a href="?action=insert">Add Meal</a></p>
<table border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    <%@taglib uri="http://example.com/functions" prefix="f" %>
    <c:forEach items="${meals}" var="meal">
        <tr style="color: ${meal.excess ? 'red' : 'green'}">
            <td><c:out value="${f:formatLocalDateTime(meal.dateTime, 'yyyy-MM-dd hh:mm')}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="?action=edit&id=${meal.id}">Update</a></td>
            <td><a href="?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>

</table>
</body>
</body>
</html>
