package ru.javawebinar.topjava.web.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({"classpath:spring/springtest-app.xml",
        "classpath:/spring/spring-app.xml",
        "classpath:/spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Ignore
public class InMemoryAdminRestControllerSpringTest {

    @Autowired
    private AdminRestController controller;

    @Qualifier(value = "inMemoryUserRepository")
    private InMemoryUserRepository inMemoryUserRepository;

    @Before
    public void setUp() {
        inMemoryUserRepository.init();
    }

    @Test
    public void delete() {
        controller.delete(USER_ID);
        Assert.assertNull(inMemoryUserRepository.get(USER_ID));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }
}