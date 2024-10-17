package org.vaskozlov.web2.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozlov.web2.lib.AutoWriteUnlock;
import org.vaskozlov.web2.service.ContextSynchronizationService;

import java.io.IOException;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    private final AreaCheckServlet areaCheckServlet = new AreaCheckServlet();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/check_point")) {
            areaCheckServlet.doGet(request, response);
            return;
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/remove_points")) {
            removeResponses(request);
            return;
        }

        doGet(request, response);
    }

    private void removeResponses(HttpServletRequest request) {
        try (var ignored = AutoWriteUnlock.lock(ContextSynchronizationService.lock)) {
            request.getServletContext().removeAttribute("responses");
        }
    }
}
