<%@ page import="org.vaskozlov.web2.lib.TimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <link href="<%=request.getContextPath()%>/resources/CSS/styles.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraph.css" rel="stylesheet" type="text/css">
    <title>Lab2 webpage to check if point is in area</title>
</head>
<body>
<main>
    <div id="page-header">
        <h1>
            Лабораторная работа №2
        </h1>
        <h2>
            Выполнил: Козлов Василий Сергеевич P3215, вариант 228
        </h2>
    </div>
    <div id="results-table-container">
        <table class="result-table" id="area-check-results-table">
            <tr>
                <th>x</th>
                <th>y</th>
                <th>r</th>
                <th>is in area?</th>
                <th>execution time</th>
            </tr>
            <jsp:useBean id="responseResults" scope="application"
                         type="java.util.List<org.vaskozlov.web2.lib.ResponseResult>"/>
            <c:forEach var="result" items="${responseResults}">
                <tr>
                    <td><f:formatNumber value="${result.x}" type="number" maxFractionDigits="2"/></td>
                    <td><f:formatNumber value="${result.y}" type="number" maxFractionDigits="2"/></td>
                    <td><f:formatNumber value="${result.r}" type="number" maxFractionDigits="2"/></td>
                    <td><c:out value="${result.inArea ? 'yes' : 'no'}"/></td>
                    <td><c:out value="${TimeFormatter.formatExecutionTime(result.executionTimeNs)}"/></td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div id="input-space-container">
        <div class="jxgbox" id="box1">

        </div>
        <jsp:include page="form.jsp"/>
    </div>
</main>
<script src="resources/JS/script.js" type="module"></script>
</body>
</html>
