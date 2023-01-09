package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> userMealRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        userMealRepository.putIfAbsent(userId, new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            getByUserId(userId).put(meal.getId(), meal);
            return meal;
        }
        if (!isOwner(meal.getId(), userId)) return null;
        // handle case: update, but not present in storage
        return getByUserId(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (isOwner(id, userId)) return getByUserId(userId).remove(id) != null;
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (!isOwner(id, userId)) return null;
        return userMealRepository.get(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId, Predicate<Meal> predicate) {
        return getByUserId(userId).values()
                .stream()
                .filter(meal -> meal.getUserId() == userId && predicate.test(meal))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getByUserId(int userId) {
        if (!userMealRepository.containsKey(userId)) return Collections.EMPTY_MAP;
        return userMealRepository.get(userId);
    }

    private boolean isOwner(int id, int userId) {
        Meal optionalMeal = getByUserId(userId).get(id);
        if (Objects.isNull(optionalMeal)) return false;
        return optionalMeal.getUserId() == userId;
    }
}

