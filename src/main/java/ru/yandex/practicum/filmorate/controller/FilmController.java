package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Начинаем получение всех фильмов");
        final Collection<Film> films = filmService.getAllFilms();
        log.info("Закончено получение всех фильмов");
        return films;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(value = "id") Long id) {
        log.info("Начинаем получение фильма с id = {}", id);
        final Film film = filmService.getFilmById(id);
        log.info("Закончено получение фильма с id = {}", id);
        return film;
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> getFilmsByDirectorId(@PathVariable(value = "directorId") Long directorId,
                                                 @RequestParam(value = "sortBy", required = false)
                                                 Collection<String> sort) {
        log.info("Начинаем получение фильмов режиссера с id = {}", directorId);
        final Collection<Film> films = filmService.getFilmsByDirectorId(directorId, sort);
        log.info("Начинаем получение фильмов режиссера с id = {}", directorId);
        return films;
    }

    @GetMapping("/search")
    public Collection<Film> getFilmsBySearch(@RequestParam(value = "query") String query,
                                             @RequestParam(value = "by") Collection<String> by) {
        log.info("Начинаем поиск фильмов");
        final Collection<Film> films = filmService.getFilmsBySearch(query, by);
        log.info("Закончен поиск фильмов");
        return films;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Начинаем добавление фильма");
        final Film savedFilm = filmService.create(film);
        log.info("Закончено добавление фильма");
        return savedFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Начинаем обновление фильма");
        final Film savedFilm = filmService.update(newFilm);
        log.info("Закончено обновление фильма");
        return savedFilm;
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable(value = "filmId") Long id) {
        log.info("Начинаем удаление фильма с id = {}", id);
        filmService.deleteFilm(id);
        log.info("Закончено удаление фильма с id = {}", id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(value = "id") Long id,
                        @PathVariable(value = "userId") Long userId) {
        log.info("Начинаем добавление \"лайка\" фильму с id = {}", id);
        filmService.addLike(id, userId);
        log.info("Закончено добавление \"лайка\" фильму с id = {}", id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable(value = "id") Long id,
                           @PathVariable(value = "userId") Long userId) {
        log.info("Начинаем удаление \"лайка\" фильму с id = {}", id);
        filmService.deleteLike(id, userId);
        log.info("Закончено удаление \"лайка\" фильму с id = {}", id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10") Long count,
                                            @RequestParam(value = "genreId", required = false) Integer genreId,
                                            @RequestParam(value = "year", required = false) Integer year) {
        log.info("Начинаем получение популярных фильмы");
        final Collection<Film> popularFilms = filmService.getPopularFilms(count, genreId, year);
        log.info("Закончено получение популярных фильмов");
        return popularFilms;
    }

    @GetMapping("/common")
    public Collection<Film> getCommonFilms(@RequestParam(value = "userId") Long userId,
                                           @RequestParam(value = "friendId") Long friendId) {
        log.info("Начинаем получение общих фильмов у друзей");
        final Collection<Film> commonFilms = filmService.getCommonFilms(userId, friendId);
        log.info("Закончено получение общих фильмов у друзей");
        return commonFilms;
    }
}
