<%--
  Created by IntelliJ IDEA.
  project: mehdifallahi
  Date: 3/3/19
  Time: 12:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<ul>
    <li>
        id : <c:out value="${requestScope.project.getId()}"/>
    </li>

    <li>
        image : <img src="${requestScope.project.getImageUrl()}" style="width: 300px; height: 300;"/>
    </li>

    <li>
        title : <c:out value="${requestScope.project.getTitle()}"/>
    </li>

    <li>
        description : <c:out value="${requestScope.project.getDescription()}"/>
    </li>
    <li>
        skills that needed :
        <ul>
            <c:forEach var="skill" items="${requestScope.project.getSkills()}">
                <li>
                    <td dir="rtl"><c:out value="${skill.getKnowledge().getName()}"/></td>
                    :
                    <td dir="rtl"><c:out value="${skill.getPoints()}"/></td>
                </li>


            </c:forEach>
        </ul>
    </li>

    <c:if test="${!requestScope.isBided}">
        <form action="project" method="post">
            <li>
                <input type="number" name="bid">
                <button> bid</button>
                <input type="hidden" name="projectId" value="${requestScope.project.getId()}">
                <input type="hidden" name="userId" value="${requestScope.user.getId()}">
            </li>
        </form>
    </c:if>

</ul>

</body>
</html>
