package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalTime, Integer> caloriesSumByDate = new HashMap<>();
        for(UserMeal userMeal : meals){
            LocalTime localTime = userMeal.getDateTime().toLocalTime();
            caloriesSumByDate.put(localTime,
                    caloriesSumByDate.getOrDefault(localTime,0) +
                            userMeal.getCalories());
        }

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        for(UserMeal meal : meals){
            LocalDateTime localDateTime = meal.getDateTime();
            if(TimeUtil.isBetweenHalfOpen(localDateTime.toLocalTime(),startTime,endTime)){
                mealsWithExcess.add(new UserMealWithExcess(localDateTime,meal.getDescription(),
                        meal.getCalories(),caloriesSumByDate.get(localDateTime.toLocalTime()) < caloriesPerDay));
            }
        }

        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalTime, Integer> caloriesSumByDate = meals.stream().collect(Collectors.groupingBy(e -> e.getDateTime().toLocalTime(),
                Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream().filter(e -> TimeUtil.isBetweenHalfOpen(e.getDateTime().toLocalTime(),
                        startTime,endTime))
                .map(e -> new UserMealWithExcess(e.getDateTime(),e.getDescription(),e.getCalories(),
                        caloriesSumByDate.get(e.getDateTime().toLocalTime()) < caloriesPerDay))
                .collect(Collectors.toList());
    }
}
