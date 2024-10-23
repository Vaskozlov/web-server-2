package org.vaskozlov.web2.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozlov.web2.lib.RequestValidationError;
import org.vaskozlov.web2.service.ContextSynchronizationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

@WebServlet(urlPatterns = {"/check", "/remove_points"}, loadOnStartup = 1, asyncSupported = true)
public class ControllerServlet extends HttpServlet {
    private final AreaCheckServlet areaCheckServlet = new AreaCheckServlet();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Locale.setDefault(Locale.ENGLISH); // Афанас предпочитает запятые в дробях...

        try (var ignored = ContextSynchronizationService.applyWriteLock()) {
            getServletContext().setAttribute("responseResults", new ArrayList<>());
        }

        areaCheckServlet.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        final String path = request.getServletPath();

        if (path.equals("/check")) {
            areaCheckServlet.doGet(request, response);
            return;
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        final String path = request.getServletPath();

        if (path.equals("/remove_points")) {
            removeResponsesFromContext();
            return;
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        request.setAttribute(
                "occurredError",
                new RequestValidationError(
                        "ControllerServlet",
                        "Invalid path for a delete request"
                )
        );

        request.getRequestDispatcher("/error_page.jsp").forward(request, response);
    }

    private void removeResponsesFromContext() {
        try (var ignored = ContextSynchronizationService.applyWriteLock()) {
            ArrayList<?> responses = (ArrayList<?>) getServletContext().getAttribute("responseResults");
            responses.clear();
        }
    }
}
