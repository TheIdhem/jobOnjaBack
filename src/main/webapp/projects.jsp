<%--
  Created by IntelliJ IDEA.
  User: mehdifallahi
  Date: 3/1/19
  Time: 11:55 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>projects</title>
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
            <td>
                <a href="/project?projectId=${project.getId()}&userId=${requestScope.user.getId()}">
                    <c:out value="${project.getId()}"/>
                </a>
            </td>
            <td dir="rtl"><c:out value="${project.getTitle()}"/></td>
            <td>${project.getBudget()}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>


</body>
</html>
