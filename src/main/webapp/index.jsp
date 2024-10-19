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
            <%
                final List<ResponseResult> responses =
                        (List<ResponseResult>) ContextSynchronizationService.readFromContext(
                                request.getServletContext(),
                                "responses"
                        );

                if (responses != null) {
                    for (final ResponseResult resp : responses) {
            %>
            <tr>
                <td><%="%.2f".formatted(resp.x())%>
                </td>
                <td><%="%.2f".formatted(resp.y())%>
                </td>
                <td><%="%.2f".formatted(resp.r())%>
                </td>
                <td><%=resp.isInArea() ? "yes" : "no"%>
                </td>
                <td><%=TimeFormatter.formatExecutionTime(resp.executionTimeNs())%>
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
<script src="resources/JS/script.js" type="module"></script>
</body>
</html>
