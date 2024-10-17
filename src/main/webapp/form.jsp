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
        <%
            for (double r : RequestDataValidationService.getAVAILABLE_R_VALUES()) {
        %>
        <div>
            <input id="r=<%=r%>" name="<%=r%>" type="checkbox"/>
            <label for="r=<%=r%>">
                R = <%=r%>
            </label>
        </div>
        <%
            }
        %>
    </fieldset>
    <button type="submit">Submit</button>
    <button type="button" id="clear_table_button">Clear table</button>
</form>
