<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.vaskozlov.web2.lib.ResponseResult" %>
<%@ page import="org.vaskozlov.web2.lib.TimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <link href="<%=request.getContextPath()%>/resources/CSS/styles.css" rel="stylesheet">
    <title>Lab2 webpage</title>
</head>
<%
    final ResponseResult lastResponse = (ResponseResult) request.getAttribute("responseResult");
    response.setStatus(200 + (lastResponse.isInArea() ? 0 : 1));
%>
<body>
<main>
    <div id="page-header">
        <jsp:include page="standart_page_header.jsp"/>
        <h3>
            Страница для проверки результата попадания
        </h3>
    </div>
    <div id="results-table-container">
        <table class="result-table" id="area-check-results-table">
            <tr>
                <th>x</th>
                <th>y</th>
                <th>r</th>
                <th>is in area?</th>
                <th>execution time (ns)</th>
            </tr>
            <jsp:useBean id="responseResult" scope="request" class="org.vaskozlov.web2.lib.ResponseResult"/>
            <tr>
                <td><f:formatNumber value="${responseResult.x}" type="number" maxFractionDigits="2"/></td>
                <td><f:formatNumber value="${responseResult.y}" type="number" maxFractionDigits="2"/></td>
                <td><f:formatNumber value="${responseResult.r}" type="number" maxFractionDigits="2"/></td>
                <td><c:out value="${responseResult.inArea ? 'yes' : 'no'}"/></td>
                <td><c:out value="${TimeFormatter.formatExecutionTime(responseResult.executionTimeNs)}"/></td>
            </tr>
        </table>
    </div>
    <div id="input-space-container">
        <h1>
            <a href="<%=request.getContextPath()%>/index.jsp">Back to main page</a>
        </h1>
    </div>
</main>
</body>
</html>
