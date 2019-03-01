<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>projects</title>
    <meta charset="UTF-8">
    <style>
        table {
            text-align: center;
            margin: 30px auto;
        }
    </style>
</head>
<body>


<table>
    <tbody>
    <tr>
        <th>ID</th>
        <th>TITLE</th>
        <th>BUDGET</th>
    </tr>
    <c:forEach var="project" items="${requestScope.projects}">
        <tr>
            <td dir="rtl"><c:out value="${project.getId()}"/></td>
            <td dir="rtl"><c:out value="${project.getTitle()}"/></td>
            <td dir="rtl">${project.getBudget()}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>


</body>
</html>
