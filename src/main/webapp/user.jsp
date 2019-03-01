<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mehdifallahi
  Date: 3/1/19
  Time: 12:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
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

</ul>

</body>
</html>
