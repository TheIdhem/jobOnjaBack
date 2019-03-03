<%--
  Created by IntelliJ IDEA.
  User: mehdifallahi
  Date: 3/1/19
  Time: 8:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>users info</title>
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
        <th>NAME</th>
        <th>BIO</th>
    </tr>
    <c:forEach var="user" items="${requestScope.users}">
        <tr>
            <c:choose>
                <c:when test="${!requestScope.user.getId().equals(user.getId())}">
                    <td>
                    <a href="/user/info?userId=${user.getId()}">
                        <c:out value="${user.getId()}"/>
                        <input type="hidden" name="method" value="endorseSkill">
                    </a>
                    </td>
                </c:when>
                <c:otherwise>
                    <td>
                    <a href="/user/profile/${user.getId()}">
                        <c:out value="${user.getId()}"/>
                        <input type="hidden" name="method" value="endorseSkill">
                    </a>
                    </td>
                </c:otherwise>
            </c:choose>
            <td dir="rtl"><c:out value="${user.getFirstName()}"/></td>
            <td dir="rtl">${user.getBio()}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
