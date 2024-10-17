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
    <title>Lab2 webpage</title>
</head>
<%

    final List<ResponseResult> responses = (List<ResponseResult>) ContextSynchronizationService.readFromContext(request, "responses");

    if (responses == null || responses.isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    final ResponseResult lastResponse = responses.getLast();
%>
<body>
<main>
    <div id="results-table-container">
        <table class="result-table" id="area-check-results-table">
            <tr>
                <th>x</th>
                <th>y</th>
                <th>r</th>
                <th>is in area?</th>
                <th>execution time (ns)</th>
            </tr>
            <tr>
                <td>
                    <%=lastResponse.x()%>
                </td>
                <td>
                    <%=lastResponse.y()%>
                </td>
                <td>
                    <%=lastResponse.r()%>
                </td>
                <td>
                    <%=lastResponse.isInArea() ? "yes" : "no"%>
                </td>
                <td>
                    <%=TimeFormatter.formatTime(lastResponse.executionTime())%>
                </td>
            </tr>
        </table>
    </div>
</main>
</body>
</html>
