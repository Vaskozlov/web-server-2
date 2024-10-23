<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.vaskozlov.web2.service.RequestDataValidationService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form method="GET" autocomplete="off" id="main-form">
    <p>
        <label for="x_input">
            Select x:
            <div id="x_input">
                <c:forEach var="i" begin="0" end="8">
                    <button type="button" class="x-button" value="${i-4}">${i-4}</button>
                </c:forEach>
            </div>
        </label>
    </p>
    <p>
        <label for="y_input">
            y:
            <input id="y_input" type="number" placeholder="-5..5" min="-5" max="5" step="0.1" required/>
        </label>
    </p>
    <label for="r_selector">
        Select R:
        <select id="r_selector">
            <option value="NaN">Nan</option>
            <c:forEach var="r" items="<%= RequestDataValidationService.getAvailableRValues() %>">
                <option value="${r}">${r}</option>
            </c:forEach>
        </select>
    </label>
    <div></div>
    <span id="r_selector_error" style="color: red; display: none;">
        Unable to calculate point coordinates, please select R
    </span>
    <div></div>
    <button type="submit" id="submit_button">Submit</button>
    <button type="button" id="clear_button">Clear table</button>
</form>
