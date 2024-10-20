<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.vaskozlov.web2.lib.ResponseResult" %>
<%@ page import="org.vaskozlov.web2.lib.TimeFormatter" %>
<%@ page import="org.vaskozlov.web2.service.ContextSynchronizationService" %>
<%@ page import="java.util.List" %>
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
        <jsp:include page="standart_page_header.jsp"/>
    </div>
    <div id="results-table-container">
        <table class="result-table" id="area-check-results-table">
            <tr>
                <jsp:include page="standart_table_header.jsp"/>
            </tr>
            <jsp:useBean id="responses" scope="application"
                         type="java.util.List<org.vaskozlov.web2.lib.ResponseResult>"/>
            <c:if test="${responses != null}">
                <c:forEach var="resp" items="${responses}">
                    <tr>
                        <td><c:out value="${String.format('%.2f', resp.x())}"/></td>
                        <td><c:out value="${String.format('%.2f', resp.y())}"/></td>
                        <td><c:out value="${String.format('%.2f', resp.r())}"/></td>
                        <td><c:out value="${resp.isInArea() ? 'yes' : 'no'}"/></td>
                        <td><c:out value="${TimeFormatter.formatExecutionTime(resp.executionTimeNs())}"/></td>
                    </tr>
                </c:forEach>
            </c:if>
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
