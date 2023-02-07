package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, false);
        applicationContext.setConfigLocations("spring/spring-db.xml", "spring/spring-app.xml");
        applicationContext.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile());
        applicationContext.refresh();
    }
}
