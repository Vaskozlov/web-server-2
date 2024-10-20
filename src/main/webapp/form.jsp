<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.vaskozlov.web2.service.RequestDataValidationService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form method="GET" autocomplete="off" id="main-form">
    <p>
        <label for="x_input">
            x:
            <input id="x_input" type="number" placeholder="-3..3" min="-3" max="3" step="0.1" required/>
        </label>
    </p>
    <p>
        <label for="y_input">
            y:
            <input id="y_input" type="number" placeholder="-5..5" min="-5" max="5" step="0.1" required/>
        </label>
    </p>
    <fieldset>
        <legend>
            Choose R
        </legend>
        <c:forEach var="r" items="<%= RequestDataValidationService.getAvailableRValues() %>">
            <div>
                <input type="checkbox" id="r=${r}" class="r_value_checkbox" value="${r}"/>
                <label for="r=${r}">
                    R = ${r}
                </label>
            </div>
        </c:forEach>
    </fieldset>
    <button type="submit">Submit</button>
    <button type="button" id="clear_button">Clear table</button>
</form>
