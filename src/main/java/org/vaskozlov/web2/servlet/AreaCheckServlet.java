package org.vaskozlov.web2.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozlov.web2.lib.*;
import org.vaskozlov.web2.service.AreaCheckService;
import org.vaskozlov.web2.service.ContextSynchronizationService;
import org.vaskozlov.web2.service.RequestDataValidationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AreaCheckServlet")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final long executionBegin = System.nanoTime();
        response.setContentType("text/html");

        final String x = request.getParameter("x");
        final String y = request.getParameter("y");
        final String r = request.getParameter("r");

        final Result<TransformedRequestData, RequestValidationError> dataValidationResult =
                RequestDataValidationService.validateRequestData(x, y, r);

        if (dataValidationResult.isError()) {
            final RequestValidationError error = dataValidationResult.getError();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, error.message());
            return;
        }

        final TransformedRequestData transformedData = dataValidationResult.getValue();
        final boolean isInArea = AreaCheckService.isInArea(transformedData.x(), transformedData.y(), transformedData.r());

        final long executionTime = System.nanoTime() - executionBegin;

        final ResponseResult currentResult = new ResponseResult(
                transformedData.x(),
                transformedData.y(),
                transformedData.r(),
                isInArea,
                executionTime
        );

        try (var ignored = AutoWriteUnlock.lock(ContextSynchronizationService.lock)) {
            final ServletContext context = request.getServletContext();
            List<ResponseResult> responses = (List<ResponseResult>) context.getAttribute("responses");

            if (responses == null) {
                responses = new ArrayList<>();
            }

            responses.add(currentResult);
            context.setAttribute("responses", responses);
        }

        request.getRequestDispatcher("/check_result.jsp").forward(request, response);
    }
}
