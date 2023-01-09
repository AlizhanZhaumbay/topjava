package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService service;
    private final Logger log = LoggerFactory.getLogger(MealRestController.class);

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id {}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public Collection<MealTo> getAll() {
        log.info("getAll by id {}", authUserId());
        return MealsUtil.getTos(service.getAll(authUserId(), meal -> true), authUserCaloriesPerDay());
    }

    public Collection<MealTo> getAll(LocalDateTime startDate, LocalTime startTime,
                                     LocalDateTime endDate, LocalTime endTime) {
        log.info("getAll 'with filters' by id {}", authUserId());
        return MealsUtil.getTos(service.getAll(authUserId(),
                                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)
                                && DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(),startDate,endDate)),
                authUserCaloriesPerDay());
    }

}