package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    FilmController fc = new FilmController();
    Film film;

    @BeforeEach
    void beforeEach() {
        film = new Film();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void shouldReturnExceptionWhenIncorrectName(String name) {
        film.setName(name);
        film.setDescription("Example");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.now());

        assertThrows(ValidationException.class, () -> fc.checkFilm(film), "Проверка пройдена");

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription" +
            "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription" +
            "DescriptionDescriptionDescription"})
    void shouldReturnExceptionWhenIncorrectDescription(String description) {
        film.setName("name");
        film.setDescription(description);
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.now());

        assertThrows(ValidationException.class, () -> fc.checkFilm(film), "Проверка пройдена");
    }

    @ParameterizedTest
    @ValueSource(strings = {"DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription" +
            "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription" +
            "DescriptionDe", "Description", ""})
    void shouldReturnOkWhenCorrectDescription(String description) {
        film.setName("name");
        // Длина описания равна 200 символов
        film.setDescription(description);
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.now());

        assertDoesNotThrow(() -> fc.checkFilm(film), "Проверка не пройдена");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "1800-01-01")
    void shouldReturnExceptionWhenIncorrectDate(LocalDate localDate) {
        film.setName("name");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(localDate);

        assertThrows(ValidationException.class, () -> fc.checkFilm(film), "Проверка пройдена");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1895-12-28", "1900-01-01"})
    void shouldReturnOkWhenCorrectDate(LocalDate localDate) {
        film.setName("name");
        film.setDescription("description");
        film.setDuration(100L);
        film.setReleaseDate(localDate);

        assertDoesNotThrow(() -> fc.checkFilm(film), "Проверка не пройдена");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1L, 0L})
    void shouldReturnExceptionWhenIncorrectDuration(Long duration) {
        film.setName("name");
        film.setDescription("description");
        film.setDuration(duration);
        film.setReleaseDate(LocalDate.of(1995, 12, 28));

        assertThrows(ValidationException.class, () -> fc.checkFilm(film), "Проверка пройдена");
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L})
    void shouldReturnOkWhenIncorrectDuration(Long duration) {
        film.setName("name");
        film.setDescription("description");
        film.setDuration(duration);
        film.setReleaseDate(LocalDate.of(1995, 12, 28));

        assertDoesNotThrow(() -> fc.checkFilm(film), "Проверка не пройдена");
    }
}
