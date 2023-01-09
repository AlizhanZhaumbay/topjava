package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MealRestController mealRestController = applicationContext.getBean(MealRestController.class);
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String userId = request.getParameter("userId");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if(!userId.isEmpty()) meal.setUserId(Integer.parseInt(userId));

        if (meal.isNew()) {
            log.info("Create {}", meal);
            mealRestController.create(meal);
        } else {
            log.info("Update {}", meal);

            mealRestController.update(meal, getId(request));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        MealRestController mealRestController = applicationContext.getBean(MealRestController.class);

        String action = request.getParameter("action");


        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal mealTo = "create".equals(action) ?
                        new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000)
                        : mealRestController.get(getId(request));
                request.setAttribute("meal", mealTo);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                Collection<MealTo> mealsTos;
                if(request.getParameterMap().isEmpty()) mealsTos = mealRestController.getAll();
                else{
                    String startDate = request.getParameter("startDate");
                    String startTime = request.getParameter("startTime");
                    String endDate = request.getParameter("endDate");
                    String endTime = request.getParameter("endTime");

                    mealsTos = mealRestController.getAll(
                            startDate.isEmpty() ? LocalDateTime.MIN : LocalDateTime.parse(startDate),
                            startTime.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTime),
                            endDate.isEmpty() ? LocalDateTime.MAX : LocalDateTime.parse(endDate),
                            endTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTime)
                    );
                }
                request.setAttribute("meals",
                        mealsTos);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        applicationContext.close();
    }
}