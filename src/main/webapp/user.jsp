<%--
  Created by IntelliJ IDEA.
  User: mehdifallahi
  Date: 3/1/19
  Time: 12:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Mehdi to yek kooni hast</title>
</head>
<body>
<ul>
    <li>
        first_name : <c:out value="${requestScope.user.getFirstName()}"/>
    </li>

    <li>
        last_name : <c:out value="${requestScope.user.getLastName()}"/>
    </li>

    <li>
        jobTitle : <c:out value="${requestScope.user.getJobTitle()}"/>
    </li>

    <li>
        bio : <c:out value="${requestScope.user.getBio()}"/>
    </li>
    <li>
        skill :
        <ul>
            <c:forEach var="skill" items="${requestScope.user.getSkills()}">
                <li>
                    <td dir="rtl"><c:out value="${skill.getKnowledge().getName()}"/></td>
                    :
                    <td dir="rtl"><c:out value="${skill.getPoints()}"/></td>
                </li>

                <form action="user" method="post">
                    <li>
                        <button> delete</button>
                        <input type="hidden" name="name" value="${skill.getKnowledge().getName()}">
                        <input type="hidden" name="point" value="${skill.getPoints()}">
                        <input type="hidden" name="userId" value="${requestScope.user.getId()}">
                        <input type="hidden" name="method" value="deleteSkill">
                    </li>
                </form>

            </c:forEach>
        </ul>
    </li>

</ul>

</body>
</html>
