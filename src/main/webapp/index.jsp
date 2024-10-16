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
    <div class="jxgbox" id="box1">

    </div>
</main>
<script src="resources/JS/result.js" type="module"></script>
<script src="resources/JS/string_to_float.js" type="module"></script>
<script src="resources/JS/board.js" type="module"></script>
<script src="resources/JS/script.js" type="module"></script>
</body>
</html>
