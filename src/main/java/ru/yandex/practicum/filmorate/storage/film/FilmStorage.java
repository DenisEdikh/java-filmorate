package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilms(Long count, Integer genreId, Integer year);

    Collection<Film> getFilmsByDirectorId(Long directorId);

    Collection<Film> getFilmsByDirectorIdOrderByLikes(Long directorId);

    Collection<Film> getFilmsByDirectorIdOrderByYear(Long directorId);

    Collection<Film> getCommonFilms(Long userId, Long friendId);

    Collection<Film> getFilmsByNameAndDirectorSearch(String query);

    Collection<Film> getFilmsByNameSearch(String query);

    Collection<Film> getFilmsByDirectorSearch(String query);

    Optional<Film> getFilmById(Long id);

    Collection<Film> getRecommendedFilms(Long id);

    Film create(Film film);

    Film update(Film newFilm);

    void deleteFilm(Long id);

    void createLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);
}
