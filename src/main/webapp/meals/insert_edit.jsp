<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 05.01.2023
  Time: 0:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<form METHOD="post" action="meals">
    <input type="hidden" value="${meal.id}" name="id"/>
    <label for="dateTime">Date Time:</label>
    <input type="datetime-local" value="<c:out value="${meal.dateTime}"/>" name="dateTime" id="dateTime">
    <br/>
    <label for="description">Description:</label>
    <input type="text" value="<c:out value="${meal.description}"/>" name = "description" id="description">
    <br/>
    <label for="calories">Calories:</label>
    <input type="number" value="<c:out value="${meal.calories}"/>" name = "calories" id="calories">
    <br/>
    <input type="submit" value="Save">
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>
