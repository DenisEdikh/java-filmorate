package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.User;

public class UserControllerTest {
    UserController uc = new UserController();
    User user;

    @BeforeEach
    void beforeEach() {
        user = new User();
    }
}
