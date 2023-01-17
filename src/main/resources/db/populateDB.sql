DELETE FROM user_roles;
DELETE FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(user_id, datetime, description, calories)
VALUES (100000,'2004-01-01 00:00','Тестовая еда',300);
INSERT INTO meals(user_id, datetime, description, calories)
VALUES (100000,'2020-12-01 12:58','Бешбармак',1235);
INSERT INTO meals(user_id, datetime, description, calories)
VALUES (100001,'2021-07-06 00:51','Админская еда',5343);
