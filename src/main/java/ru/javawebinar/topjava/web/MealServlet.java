package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final MealDAO mealDAO;
    private final static Logger log = LoggerFactory.getLogger(MealServlet.class);

    private final static String INSERT_OR_EDIT = "meals/insert_edit.jsp";
    private final static String LIST = "meals/meals.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String forward = LIST;
        String action;
        if (request.getParameterMap().containsKey("action")) action = request.getParameter("action");
        else action = "list";

        switch (action) {
            case "insert":
                forward = INSERT_OR_EDIT;
                break;
            case "edit": {
                forward = INSERT_OR_EDIT;
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("meal", mealDAO.findById(id));
                break;
            }
            case "delete": {
                int id = Integer.parseInt(request.getParameter("id"));
                mealDAO.delete(id);
                break;
            }
        }

        request.setAttribute("meals", mealDAO.filterWithoutTime());
        log.debug("forward to meals");
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        int calories = Integer.parseInt(request.getParameter("calories"));
        String optional_id = request.getParameter("id");
        if (Objects.isNull(optional_id) || optional_id.isEmpty()){
            mealDAO.add(new Meal(dateTime,description,calories));
        }
        else{
            mealDAO.update(Integer.parseInt(optional_id),new Meal(dateTime,description,calories));
        }
        request.setAttribute("meals",mealDAO.filterWithoutTime());
        request.getRequestDispatcher(LIST).forward(request,response);
    }

    static {
        mealDAO = new MealDAO();
    }
}
