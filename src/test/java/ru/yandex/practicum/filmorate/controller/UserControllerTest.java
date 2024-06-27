package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController uc = new UserController();
    User user;

    @BeforeEach
    void beforeEach() {
        user = new User();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void shouldReturnExceptionWhenIncorrectEmail(String email) {
        user.setEmail(email);
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1987, 10, 11));

        assertThrows(ValidationException.class, () -> uc.checkUser(user), "Проверка пройдена");
    }

    @Test
    void shouldReturnOkWhenCorrectEmail() {
        user.setEmail("email@");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1987, 10, 11));

        assertDoesNotThrow(() -> uc.checkUser(user), "Проверка не пройдена");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n", "login login"})
    void shouldReturnExceptionWhenIncorrectLogin(String login) {
        user.setEmail("email@email");
        user.setLogin(login);
        user.setBirthday(LocalDate.of(1987, 10, 11));

        assertThrows(ValidationException.class, () -> uc.checkUser(user), "Проверка пройдена");
    }

    @Test
    void shouldReturnOkWhenCorrectLogin() {
        user.setEmail("email@email");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1987, 10, 11));

        assertDoesNotThrow(() -> uc.checkUser(user), "Проверка не пройдена");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "4222-10-10")
    void shouldReturnExceptionWhenIncorrectDate(LocalDate date) {
        user.setEmail("email@email");
        user.setLogin("login");
        user.setBirthday(date);

        assertThrows(ValidationException.class, () -> uc.checkUser(user), "Проверка пройдена");
    }

    @Test
    void shouldReturnOkWhenCorrectDate() {
        user.setEmail("email@email");
        user.setLogin("login");
        user.setBirthday(LocalDate.now());

        assertDoesNotThrow(() -> uc.checkUser(user), "Проверка не пройдена");
    }
}
