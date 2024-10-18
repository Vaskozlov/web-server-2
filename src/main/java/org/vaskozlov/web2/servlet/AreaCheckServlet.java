package org.vaskozlov.web2.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozlov.web2.lib.RequestParameters;
import org.vaskozlov.web2.lib.RequestValidationError;
import org.vaskozlov.web2.lib.ResponseResult;
import org.vaskozlov.web2.lib.Result;
import org.vaskozlov.web2.service.AreaCheckService;
import org.vaskozlov.web2.service.ContextSynchronizationService;
import org.vaskozlov.web2.service.ErrorPageWriter;
import org.vaskozlov.web2.service.RequestDataValidationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AreaCheckServlet")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final long executionBeginNs = System.nanoTime();

        final var dataValidationResult = getParametersFromRequest(request);

        response.setContentType("text/html");

        if (dataValidationResult.isError()) {
            var validationError = dataValidationResult.getError();

            ErrorPageWriter.writeErrorPage(
                    validationError.component(),
                    validationError.message(),
                    response.getWriter()
            );
            return;
        }

        final ResponseResult responseResult = formResponseResult(
                executionBeginNs,
                dataValidationResult.getValue()
        );

        saveResultInContext(request, responseResult);
        request.setAttribute("responseResult", responseResult);

        request.getRequestDispatcher("/check_result.jsp").forward(request, response);
    }

    private static Result<RequestParameters, RequestValidationError> getParametersFromRequest(HttpServletRequest request) {
        return RequestDataValidationService.validateRequestData(
                request.getParameter("x"),
                request.getParameter("y"),
                request.getParameter("r")
        );
    }

    private static ResponseResult formResponseResult(long executionBegin, RequestParameters transformedData) {
        final boolean isInArea = AreaCheckService.isInArea(
                transformedData.x(),
                transformedData.y(),
                transformedData.r()
        );

        return new ResponseResult(
                transformedData.x(),
                transformedData.y(),
                transformedData.r(),
                isInArea,
                System.nanoTime() - executionBegin
        );
    }

    private static void saveResultInContext(HttpServletRequest request, ResponseResult currentResult) {
        try (var ignored = ContextSynchronizationService.applyWriteLock()) {
            final ServletContext context = request.getServletContext();
            List<ResponseResult> responses = (List<ResponseResult>) context.getAttribute("responses");

            if (responses == null) {
                responses = new ArrayList<>();
            }

            responses.add(currentResult);
            context.setAttribute("responses", responses);
        }
    }
}
