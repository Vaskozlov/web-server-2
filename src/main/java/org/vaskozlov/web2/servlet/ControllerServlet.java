package org.vaskozlov.web2.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozlov.web2.service.ContextSynchronizationService;
import org.vaskozlov.web2.service.ErrorPageWriter;

import java.io.IOException;
import java.util.Locale;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    private final AreaCheckServlet areaCheckServlet = new AreaCheckServlet();

    @Override
    public void init() throws ServletException {
        super.init();
        Locale.setDefault(Locale.ENGLISH); // афанас предпочитает запятые в дробях...
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        areaCheckServlet.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        final String path = request.getServletPath();

        if (path.equals("/check_point")) {
            areaCheckServlet.doGet(request, response);
            return;
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        final String path = request.getServletPath();

        if (path.equals("/remove_points")) {
            removeResponsesFromContext();
            return;
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        ErrorPageWriter.writeErrorPage(
                "ControllerServlet",
                "Invalid path for a delete request",
                response.getWriter()
        );
    }

    private void removeResponsesFromContext() {
        try (var ignored = ContextSynchronizationService.applyWriteLock()) {
            getServletContext().removeAttribute("responses");
        }
    }
}
