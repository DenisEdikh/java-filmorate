package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
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
    private final DirectorService directorService;
    private final MpaService mpaService;

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

    public Collection<Film> getFilmsByDirectorId(Long directorId, Collection<String> sort) {
        log.debug("Начата проверка наличия режиссера c id = {} в БД в методе getFilmsByDirectorId", directorId);
        directorService.getDirectorById(directorId);
        log.debug("Закончена проверка наличия режиссера c id = {} в БД в методе getFilmsByDirectorId", directorId);
        Collection<Film> films = List.of();
        for (String s : sort) {
            films = switch (s) {
                case "year" -> filmStorage.getFilmsByDirectorId(directorId, "year", "year");
                case "likes" -> filmStorage.getFilmsByDirectorId(directorId, "likes", "likes");
                default -> filmStorage.getFilmsByDirectorId(directorId, null, null);
            };
        }
        setFields(films);
        return films;
    }

    public Film create(Film film) {
        log.debug("Начата проверка наличия рейтинга и жанров у фильма c id = {} в методе create", film.getId());
        checkFieldsOfFilm(film);
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
        checkFieldsOfFilm(film);
        log.debug("Закончена проверка наличия рейтинга и жанров у фильма c id = {} в методе update", film.getId());
        return filmStorage.update(film);
    }

    public void deleteFilm(Long id) {
        log.debug("Начата проверка наличия фильма c id = {} в БД в методе delete", id);
        checkFilm(id);
        log.debug("Закончена проверка наличия фильма c id = {} в БД в методе delete", id);
        filmStorage.deleteFilm(id);
    }

    public Film getFilmById(Long id) {
        log.debug("Начата проверка наличия фильма c id = {} в БД в методе FilmById", id);
        final Film film = checkFilm(id);
        log.debug("Закончена проверка наличия фильма c id = {} в БД в методе FilmById", id);
        log.debug("Начата проверка наличия Mpa с id = {} в БД в методе FilmById", film.getMpa().getId());
        final Mpa mpa = mpaService.getMpaById(film.getMpa().getId());
        log.debug("Закончена проверка наличия Mpa с id = {} в БД в методе FilmById", film.getMpa().getId());
        final Collection<Genre> genres = genreDbStorage.getGenresByFilmId(id);
        final Collection<Director> directors = directorService.getDirectorsByFilmId(id);
//        final Collection<User> users = userStorage.getUsersByFilmId(id);
        film.setMpa(mpa);
        film.addGenre(genres);
        film.addDirector(directors);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("Начата проверка наличия фильма c id = {} и пользователя с id = {} в БД в методе addLike",
                filmId,
                userId);
        checkFilm(filmId);
        userService.getUserById(userId);
        log.debug("Закончена проверка наличия фильма c id = {} и пользователя с id = {} в БД в методе addLike",
                filmId,
                userId);
        filmStorage.createLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        log.debug("Начата проверка наличия фильма c id = {} и пользователя с id = {} в БД в методе deleteLike",
                filmId,
                userId);
        checkFilm(filmId);
        userService.getUserById(userId);
        log.debug("Закончена проверка наличия фильма c id = {} и пользователя с id = {} в БД в методе deleteLike",
                filmId,
                userId);
        filmStorage.deleteLike(filmId, userId);
    }

    // Метод возвращения общих фильмов у двух людей
    public Collection<Film> getCommonFilms(Long userId, Long friendId) {
        log.debug("Начата проверка наличия пользователя c id = {} и " +
                        "пользователя с id = {} в БД в методе getCommonFilms",
                userId,
                friendId);
        userService.getUserById(userId);
        userService.getUserById(friendId);
        log.debug("Закончена проверка наличия пользователя c id = {} и " +
                        "пользователя с id = {} в БД в методе getCommonFilms",
                userId,
                friendId);
        Collection<Film> films = filmStorage.getCommonFilms(userId, friendId);
        setFields(films);
        return films;
    }

    // Метод по возвращению рекомендуемых фильмов к просмотру
    public Collection<Film> getRecommendedFilms(Long id) {
        log.debug("Начата проверка наличия пользователя с id = {} в БД методе getRecommendedFilms", id);
        userService.getUserById(id);
        log.debug("Закончена проверка наличия пользователя с id = {} в БД в методе getRecommendedFilms", id);
        final Collection<Film> films = filmStorage.getRecommendedFilms(id);
        setFields(films);
        return films;
    }

    private void checkFieldsOfFilm(Film film) {
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
        for (Director director : film.getDirectors()) {
            directorService.getDirectorById(director.getId());
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
            film.addGenre(genreDbStorage.getGenresByFilmId(film.getId()));
            film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
            film.addDirector(directorService.getDirectorsByFilmId(film.getId()));
        }
    }
}
