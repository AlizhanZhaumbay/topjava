package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.AssertMatcher;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TIME_FORMATTER;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
        ASSERT_MATCHER = AssertMatcher.of("");
    }

    @Autowired
    private JdbcMealRepository jdbcMealRepository;

    private static final AssertMatcher<Meal> ASSERT_MATCHER;

    @Test
    public void save() {
        Meal newMeal = getNew();
        Meal created = jdbcMealRepository.save(newMeal, USER_ID);
        Integer id = created.getId();
        newMeal.setId(id);
        ASSERT_MATCHER.assertMatch(created, newMeal);
        ASSERT_MATCHER.assertMatch(jdbcMealRepository.get(id, USER_ID), newMeal);
    }

    @Test
    public void delete() {
        jdbcMealRepository.delete(MEAL3_ID, ADMIN_ID);
        assertNull(jdbcMealRepository.get(MEAL3_ID, ADMIN_ID));
    }

    @Test
    public void get() {
        Meal meal = jdbcMealRepository.get(MEAL2_ID, USER_ID);
        ASSERT_MATCHER.assertMatch(meal, MealTestData.meal2);
    }

    @Test
    public void getAll() {
        List<Meal> meals = jdbcMealRepository.getAll(USER_ID);
        ASSERT_MATCHER.assertMatch(meals, meal1, meal2);
    }

    @Test
    public void getBetween() {
        List<Meal> mealsBetweenTheRange = jdbcMealRepository.getBetweenHalfOpen(
                LocalDateTime.parse("2019-01-01 00:00", DATE_TIME_FORMATTER), null, USER_ID);
        ASSERT_MATCHER.assertMatch(mealsBetweenTheRange, meal2);
    }

    @Test
    public void getBetweenWithNullDates() {
        List<Meal> meals = jdbcMealRepository.getBetweenHalfOpen(null, null, USER_ID);
        ASSERT_MATCHER.assertMatch(meals, meal1, meal2);
    }
}