package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    public Collection<Film> getPopularFilms(Long count, Integer genreId, Integer year) {
        final Collection<Film> films = filmStorage.getPopularFilms(count, genreId, year);
        setFields(films);
        return films;
    }

    public Collection<Film> getAllFilms() {
        final Collection<Film> films = filmStorage.getAllFilms();
        setFields(films);
        return films;
    }

    public Film create(Film film) {
        log.debug("Начата проверка наличия рейтинга и жанров у фильма c id = {} в методе create", film.getId());
        checkMpaAndGenres(film);
        log.debug("Закончена проверка наличия рейтинга и жанров у фильма c id = {} в методе create", film.getId());
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.debug("Начата проверка наличия id у фильма в методе update");
        checkFilmId(film);
        log.debug("Закончена проверка наличия id у фильма в методе update");
        log.debug("Начата проверка наличия фильма c id = {} в БД в методе update", film.getId());
        checkFilm(film.getId());
        log.debug("Закончена проверка наличия фильма c id = {} в БД в методе update", film.getId());
        log.debug("Начата проверка наличия рейтинга и жанров у фильма c id = {} в методе update", film.getId());
        checkMpaAndGenres(film);
        log.debug("Закончена проверка наличия рейтинга и жанров у фильма c id = {} в методе update", film.getId());
        return filmStorage.update(film);
    }

    public void deleteFilm(Long id) {
        log.debug("Начата проверка наличия фильма c id = {} в методе delete", id);
        checkFilm(id);
        log.debug("Закончена проверка наличия фильма c id = {} в методе delete", id);
        filmStorage.deleteFilm(id);
    }

    public Film getFilmById(Long id) {
        log.debug("Начата проверка наличия фильма c id = {} в методе FilmById", id);
        final Film film = checkFilm(id);
        log.debug("Закончена проверка наличия фильма c id = {} в методе FilmById", id);
        log.debug("Начата проверка наличия Mpa с id = {} в методе FilmById", film.getMpa().getId());
        final Mpa mpa = checkMpa(film.getMpa().getId());
        log.debug("Закончена проверка наличия Mpa с id = {} в методе FilmById", film.getMpa().getId());
        final Collection<Genre> genres = genreDbStorage.getGenresByFilmId(id);
//        final Collection<User> users = userStorage.getUsersByFilmId(id);
        film.setMpa(mpa);
        film.addGenre(genres);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("Начата проверка наличия фильма c id = {} и пользователя с id = {} в методе addLike",
                filmId,
                userId);
        checkFilm(filmId);
        userService.getUserById(userId);
        log.debug("Закончена проверка наличия фильма c id = {} и пользователя с id = {} в методе addLike",
                filmId,
                userId);
        filmStorage.createLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        log.debug("Начата проверка наличия фильма c id = {} и пользователя с id = {} в методе deleteLike",
                filmId,
                userId);
        checkFilm(filmId);
        userService.getUserById(userId);
        log.debug("Закончена проверка наличия фильма c id = {} и пользователя с id = {} в методе deleteLike",
                filmId,
                userId);
        filmStorage.deleteLike(filmId, userId);
    }

    // Метод возвращения общих фильмов у двух людей
    public Collection<Film> getCommonFilms(Long userId, Long friendId) {
        log.debug("Начата проверка наличия пользователя c id = {} и пользователя с id = {} в методе getCommonFilms",
                userId,
                friendId);
        userService.getUserById(userId);
        userService.getUserById(friendId);
        log.debug("Закончена проверка наличия пользователя c id = {} и пользователя с id = {} в методе getCommonFilms",
                userId,
                friendId);
        Collection<Film> films = filmStorage.getCommonFilms(userId, friendId);
        setFields(films);
        return films;
    }

    private Mpa checkMpa(Integer mpaId) {
        return mpaDbStorage.getMpaById(mpaId)
                .orElseThrow(() -> {
                    log.warn("Mpa c id = {} не найден", mpaId);
                    return new NotFoundException(String.format("Mpa с id = %d не найден", mpaId));
                });
    }

    private void checkMpaAndGenres(Film film) {
        mpaDbStorage.getMpaById(film.getMpa().getId())
                .orElseThrow(() -> {
                    log.warn("Некорректный у Mpa id = {}", film.getMpa().getId());
                    return new ValidationException(String.format("Некорректный у Mpa id = %d", film.getMpa().getId()));
                });
        for (Genre genre : film.getGenres()) {
            genreDbStorage.getGenreById(genre.getId())
                    .orElseThrow(() -> {
                        log.warn("Некорректный у Genre id = {}", genre.getId());
                        return new ValidationException(String.format("Некорректный у Genre id = %d", genre.getId()));
                    });
        }
    }

    private void checkFilmId(Film film) {
        if (Objects.isNull(film.getId())) {
            log.warn("У фильма {} отсутствует id", film);
            throw new ConditionsNotMetException("id должен быть указан");
        }
    }

    // Методе проверки наличия фильма в базе данных
    private Film checkFilm(Long filmId) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> {
                    log.warn("Фильм с id = {} не найден", filmId);
                    return new NotFoundException(String.format("Фильм с id = %d не найден", filmId));
                });
    }

    // Метод установки полей в фильме
    private void setFields(Collection<Film> films) {
        for (Film film : films) {
            final Collection<Genre> genres = genreDbStorage.getGenresByFilmId(film.getId());
            film.addGenre(genres);
            film.setMpa(checkMpa(film.getMpa().getId()));
        }
    }
}
