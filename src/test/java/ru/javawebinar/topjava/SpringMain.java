package ru.javawebinar.topjava;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.service.UserService;


public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, false);
        applicationContext.setConfigLocations("spring/spring-db.xml", "spring/spring-app.xml");
        String activeProfile = Profiles.getActiveDbProfile();
        System.out.println(activeProfile);
        applicationContext.getEnvironment().setActiveProfiles(activeProfile);
        applicationContext.refresh();
        UserService userService = applicationContext.getBean(UserService.class);
        userService.getAll().forEach(System.out::println);
    }
}
