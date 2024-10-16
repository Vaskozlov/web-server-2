package org.vaskozlov.web2.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozlov.web2.lib.RequestValidationError;
import org.vaskozlov.web2.lib.Result;
import org.vaskozlov.web2.lib.TransformedRequestData;
import org.vaskozlov.web2.service.AreaCheckService;
import org.vaskozlov.web2.service.RequestDataValidationService;

import java.io.IOException;

public class AreaCheckServlet extends HttpServlet {
    private void sendError(HttpServletResponse response, RequestValidationError error) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, error.message());
    }

    private void sendSuccess(HttpServletResponse response, TransformedRequestData requestData) throws IOException {
        final boolean isInArea = AreaCheckService.isInArea(requestData.x(), requestData.y(), requestData.r());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String x = request.getParameter("x");
        final String y = request.getParameter("y");
        final String r = request.getParameter("r");

        final Result<TransformedRequestData, RequestValidationError> dataValidationResult =
                RequestDataValidationService.validateRequestData(x, y, r);

        if (dataValidationResult.isError()) {
            sendError(response, dataValidationResult.getError());
        } else {
            sendSuccess(response, dataValidationResult.getValue());
        }
    }
}
