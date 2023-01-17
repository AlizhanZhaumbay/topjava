package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TIME_FORMATTER;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int MEAL1_ID = START_SEQ + 3;
    public static final int MEAL2_ID = START_SEQ + 4;
    public static final int MEAL3_ID = START_SEQ + 5;


    public static final Meal meal1 = new Meal(MEAL1_ID, LocalDateTime.parse("2004-01-01 00:00", DATE_TIME_FORMATTER),
            "Тестовая еда", 300);
    public static final Meal meal2 = new Meal(MEAL2_ID, LocalDateTime.parse("2020-12-01 12:58",
            DATE_TIME_FORMATTER), "Бешбармак", 1235);
    public static final Meal meal3 = new Meal(MEAL3_ID, LocalDateTime.parse("2021-07-06 00:51",
            DATE_TIME_FORMATTER), "Админская еда", 5343);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now(), "Новая еда", 230);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL1_ID, LocalDateTime.parse("2020-06-17 19:38"),
                "Обновленная еда", 2300);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("dateTime").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("dateTime").isEqualTo(expected);
    }
}
