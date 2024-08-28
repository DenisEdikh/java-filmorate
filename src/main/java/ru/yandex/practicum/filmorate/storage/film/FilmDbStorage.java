package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
@Qualifier("filmDbStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> getAllFilms() {
        String findAllQuery = "SELECT * FROM films";
        return findMany(findAllQuery);
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        String findByIdQuery = "SELECT * FROM films WHERE id = ?";
        return findOne(findByIdQuery, id);
    }

    @Override
    public Film create(Film film) {
        String insertFilmQuery = "INSERT INTO films (name, " +
                "description, " +
                "release_date, " +
                "duration, " +
                "mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        String insertGenresQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES(?, ?)";

        final Long id = insert(insertFilmQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        for (Genre genre : film.getGenres()) {
            update(insertGenresQuery, id, genre.getId());
        }
        log.info("Добавили фильм с id = {}", id);
        return film;
    }

    @Override
    public Film update(Film film) {
        String updateFilmQuery = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?";
        update(updateFilmQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        log.info("Обновили фильм с id = {}", film.getId());
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        String deleteFilmQuery = "DELETE FROM films WHERE id = ?";
        update(deleteFilmQuery, id);
        log.info("Удалили фильм с id =  {}", id);
    }

    @Override
    public void createLike(Long filmId, Long userId) {
        String insertLikeQuery = "INSERT INTO likes(film_id, user_id) VALUES (?, ?)";
        update(insertLikeQuery,
                filmId,
                userId);
        log.info("Добавили like фильму с id = {}", filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String deleteLikeQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        update(deleteLikeQuery,
                filmId,
                userId);
        log.info("Удалили like фильму с id = {}", filmId);
    }

    @Override
    public Collection<Film> getPopularFilms(Long count, Integer genreId, Integer year) {
        String findPopularQuery = """
            SELECT f.* FROM films f
            LEFT JOIN likes l ON f.id = l.film_id
            LEFT JOIN film_genre fg ON f.id = fg.film_id
            WHERE (? IS NULL OR YEAR(f.release_date) = ?)
            AND (? IS NULL OR fg.genre_id = ?)
            GROUP BY f.id
            ORDER BY COUNT(l.user_id) DESC
            LIMIT ?
            """;
        return findMany(findPopularQuery, year, year, genreId, genreId, count);
    }

    @Override
    public Collection<Film> getCommonFilms(Long userId, Long friendId) {
        String findCommonFilms = """
                SELECT f.* FROM films f
                LEFT JOIN likes l1 ON f.id = l1.film_id
                LEFT JOIN likes l2 ON f.id = l2.film_id
                WHERE l1.user_id = ? AND l2.user_id = ? AND l1.film_id = l2.film_id
                GROUP BY l1.film_id
                ORDER BY COUNT(l1.user_id) DESC
                """;
        return findMany(findCommonFilms, userId, friendId);
    }

}
