<%@ page import="org.vaskozlov.web2.lib.ResponseResult" %>
<%@ page import="java.util.List" %>
<%@ page import="org.vaskozlov.web2.lib.TimeFormatter" %>
<%@ page import="org.vaskozlov.web2.service.ContextSynchronizationService" %>
<%@ page import="org.vaskozlov.web2.lib.FloatRounder" %>
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
            <tr>
                <td>
                    <%=FloatRounder.round(lastResponse.x(), 3)%>
                </td>
                <td>
                    <%=FloatRounder.round(lastResponse.y(), 3)%>
                </td>
                <td>
                    <%=FloatRounder.round(lastResponse.r(), 3)%>
                </td>
                <td>
                    <%=lastResponse.isInArea() ? "yes" : "no"%>
                </td>
                <td>
                    <%=TimeFormatter.formatTime(lastResponse.executionTimeNs())%>
                </td>
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
