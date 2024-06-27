package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmControllerTest {
    FilmController fc = new FilmController();
    Film film;

    @BeforeEach
    void beforeEach() {
        film = new Film();
    }
}
