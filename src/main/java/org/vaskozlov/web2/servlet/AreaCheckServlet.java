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
import java.util.List;

@WebServlet(urlPatterns = "/check_if_point_is_in_area", asyncSupported = true)
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final long executionBeginNs = System.nanoTime();
        final var requestParameters = getParametersFromRequest(request);

        if (requestParameters.isError()) {
            var validationError = requestParameters.getError();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            ErrorPageWriter.writeErrorPage(
                    validationError.component(),
                    validationError.message(),
                    response.getWriter()
            );
            return;
        }

        final ResponseResult responseResult = formResponseResult(
                executionBeginNs,
                requestParameters.getValue()
        );

        saveResultInContext(getServletContext(), responseResult);
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
        final boolean isInArea = new AreaCheckService(
                transformedData.x(),
                transformedData.y(),
                transformedData.r()
        ).isInArea();

        return new ResponseResult(
                transformedData.x(),
                transformedData.y(),
                transformedData.r(),
                isInArea,
                System.nanoTime() - executionBegin
        );
    }

    private static void saveResultInContext(ServletContext context, ResponseResult currentResult) {
        try (var ignored = ContextSynchronizationService.applyWriteLock()) {
            ((List<ResponseResult>) context.getAttribute("responses")).add(currentResult);
        }
    }
}
