package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to users");
        int userId = Integer.parseInt(request.getParameter("enter"));
        SecurityUtil.setAuthUserId(userId);
        log.info("SecurityUtil authUserId {}",userId);
        //request.getRequestDispatcher("/users.jsp").forward(request, response);
        response.sendRedirect("meals.jsp");
    }
}
