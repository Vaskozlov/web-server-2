<%@ page import="org.vaskozlov.web2.lib.ResponseResult" %>
<%@ page import="java.util.List" %>
<%@ page import="org.vaskozlov.web2.lib.TimeFormatter" %>
<%@ page import="org.vaskozlov.web2.service.ContextSynchronizationService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <link href="<%=request.getContextPath()%>/resources/CSS/styles.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraph.css" rel="stylesheet" type="text/css">
    <title>Lab2 webpage</title>
</head>
<body>
<main>
    <div id="page-header">
        <h1>
            Лабораторная работа №2
        </h1>
        <h2>
            Выполнил: Козлов Василий Сергеевич P3215 408811
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
            <%
                final List<ResponseResult> responses = (List<ResponseResult>) ContextSynchronizationService.readFromContext(request, "responses");

                if (responses != null) {
                    for (final ResponseResult resp : responses) {
            %>
            <tr>
                <td><%=resp.x()%>
                </td>
                <td><%=resp.y()%>
                </td>
                <td><%=resp.r()%>
                </td>
                <td><%=resp.isInArea() ? "yes" : "no"%>
                </td>
                <td><%=TimeFormatter.formatTime(resp.executionTime())%>
                </td>
            </tr>
            <%
                    }
                }
            %>
        </table>
    </div>
    <div id="input-space-container">
        <div class="jxgbox" id="box1">

        </div>
        <jsp:include page="form.jsp"/>
    </div>
</main>
<script src="resources/JS/result.js" type="module"></script>
<script src="resources/JS/string_to_float.js" type="module"></script>
<script src="resources/JS/board.js" type="module"></script>
<script src="resources/JS/script.js" type="module"></script>
</body>
</html>
