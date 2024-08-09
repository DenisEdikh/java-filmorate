package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilms();

    Optional<Film> getFilmById(Long filmId);

    Film create(Film film);

    Film update(Film newFilm);

    void createLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);
}
