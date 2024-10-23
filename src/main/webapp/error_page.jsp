<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Web lab 2 error page</title>
</head>
<body>
<jsp:useBean id="occurredError" scope="request"
             type="org.vaskozlov.web2.lib.RequestValidationError"/>
<h1>
    Error in ${occurredError.message}
</h1>
<h2>
    ${occurredError.message}
</h2>
<h2>
    <a href="<%=request.getContextPath()%>/index.jsp">Back to main page</a>
</h2>
</body>
</html>
