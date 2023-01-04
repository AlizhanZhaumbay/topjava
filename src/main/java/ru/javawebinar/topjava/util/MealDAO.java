package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDAO {
    private static final List<Meal> meals;
    private static final AtomicInteger id;
    private static final int CALORIES_PER_DAY = 2000;

    public void add(Meal meal) {
        meal.setId(id.incrementAndGet());
        meals.add(meal);
    }

    public void update(int id,Meal meal){
        Meal oldMeal = findById(id);

        oldMeal.setDescription(meal.getDescription());
        oldMeal.setCalories(meal.getCalories());
        oldMeal.setDateTime(meal.getDateTime());
    }

    public void delete(int id){
        meals.removeIf(meal -> meal.getId() == id);
    }

    public Meal findById(int id){
        for(Meal meal : meals)
            if(meal.getId() == id) return meal;

        throw new NoSuchElementException();
    }

    public List<MealTo> filterWithoutTime() {
        return MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
    }


    static {
        id = new AtomicInteger();
        meals = new ArrayList<>(Arrays.asList(
                new Meal(id.incrementAndGet(),LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(id.incrementAndGet(),LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(id.incrementAndGet(),LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(id.incrementAndGet(),LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(id.incrementAndGet(),LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(id.incrementAndGet(),LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(id.incrementAndGet(),LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
    }
}
