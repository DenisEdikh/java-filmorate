package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;
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
    public Collection<Film> getFilmsByNameAndDirectorSearch(String query) {
        String findFilmsByNameAndDirectorQuery = """
                SELECT f.* FROM films f
                LEFT JOIN film_director fd ON f.id = fd.film_id
                LEFT JOIN directors d ON fd.director_id = d.id
                LEFT JOIN likes l ON f.id = l.film_id
                WHERE f.name ILIKE CONCAT ('%', ?, '%')
                OR d.name ILIKE CONCAT ('%', ?, '%')
                GROUP BY f.id
                ORDER BY COUNT(l.user_id) DESC
                """;
        return findMany(findFilmsByNameAndDirectorQuery, query, query);
    }

    @Override
    public Collection<Film> getFilmsByNameSearch(String query) {
        String findFilmsByNameQuery = """
                SELECT f.* FROM films f
                LEFT JOIN likes l ON f.id = l.film_id
                WHERE name ILIKE CONCAT ('%', ?, '%')
                GROUP BY f.id
                ORDER BY COUNT(l.user_id) DESC
                """;
        return findMany(findFilmsByNameQuery, query);
    }

    @Override
    public Collection<Film> getFilmsByDirectorSearch(String query) {
        String findFilmsByDirectorQuery = """
                SELECT f.* FROM films f
                LEFT JOIN film_director fd ON f.id = fd.film_id
                LEFT JOIN directors d ON fd.director_id = d.id
                LEFT JOIN likes l ON f.id = l.film_id
                WHERE d.name ILIKE CONCAT ('%', ?, '%')
                GROUP BY f.id
                ORDER BY COUNT(l.user_id) DESC
                """;
        return findMany(findFilmsByDirectorQuery, query);
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        String findByIdQuery = "SELECT * FROM films WHERE id = ?";
        return findOne(findByIdQuery, id);
    }

    @Override
    public Collection<Film> getFilmsByDirectorId(Long id) {
        String findByDirectorIdQuery = """
                    SELECT f.* FROM films f
                    LEFT JOIN film_director fd ON f.id = fd.film_id
                    WHERE fd.director_id = ?
                """;
        return findMany(findByDirectorIdQuery, id);
    }

    @Override
    public Collection<Film> getFilmsByDirectorIdOrderByLikes(Long id) {
        String findByDirectorIdQuery = """
                    SELECT f.* FROM films f
                    LEFT JOIN film_director fd ON f.id = fd.film_id
                    LEFT JOIN likes l ON f.id = l.film_id
                    WHERE fd.director_id = ?
                    GROUP BY f.id
                    ORDER BY COUNT(l.user_id)
                    DESC
                """;
        return findMany(findByDirectorIdQuery, id);
    }

    @Override
    public Collection<Film> getFilmsByDirectorIdOrderByYear(Long id) {
        String findByDirectorIdQuery = """
                    SELECT f.* FROM films f
                    LEFT JOIN film_director fd ON f.id = fd.film_id
                    LEFT JOIN likes l ON f.id = l.film_id
                    WHERE fd.director_id = ?
                    ORDER BY YEAR(f.release_date) ASC
                """;
        return findMany(findByDirectorIdQuery, id);
    }

    @Override
    public Film create(Film film) {
        String insertFilmQuery = "INSERT INTO films (name, " +
                "description, " +
                "release_date, " +
                "duration, " +
                "mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        final Long id = insert(insertFilmQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        addGenresOfFilms(film);
        addDirectorsOfFilms(film);
        log.info("Добавили фильм с id = {}", id);
        return film;
    }

    @Override
    public Film update(Film film) {
        String updateFilmQuery = """
                UPDATE films
                SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?
                WHERE id = ?
                """;

        update(updateFilmQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteGenresOfFilms(film);
        addGenresOfFilms(film);
        deleteDirectorsOfFilms(film);
        addDirectorsOfFilms(film);
        log.info("Обновили фильм с id = {}", film.getId());
        return film;
    }

    private void addGenresOfFilms(Film film) {
        if (Objects.nonNull(film.getGenres()) && !film.getGenres().isEmpty()) {
            String insertGenresQuery = "MERGE INTO film_genre KEY(film_id, genre_id) VALUES(?, ?)";
            film.getGenres().forEach(g -> update(insertGenresQuery, film.getId(), g.getId()));
        }
    }

    private void deleteGenresOfFilms(Film film) {
            String deleteGenresQuery = "DELETE FROM film_genre WHERE film_id = ?";
            delete(deleteGenresQuery, film.getId());
    }

    private void addDirectorsOfFilms(Film film) {
        if (Objects.nonNull(film.getDirectors()) && !film.getDirectors().isEmpty()) {
            String insertDirectorsQuery = "MERGE INTO film_director KEY(film_id, director_id) VALUES(?, ?)";
            film.getDirectors().forEach(d -> update(insertDirectorsQuery, film.getId(), d.getId()));
        }
    }

    private void deleteDirectorsOfFilms(Film film) {
            String deleteDirectorsQuery = "DELETE FROM film_director WHERE film_id = ?";
            delete(deleteDirectorsQuery, film.getId());
    }


    @Override
    public void deleteFilm(Long id) {
        String deleteFilmQuery = "DELETE FROM films WHERE id = ?";
        update(deleteFilmQuery, id);
        log.info("Удалили фильм с id =  {}", id);
    }

    @Override
    public void createLike(Long filmId, Long userId) {
        String insertLikeQuery = "MERGE INTO likes KEY(film_id, user_id) VALUES (?, ?)";
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
        String findCommonFilmsQuery = """
                SELECT f.* FROM films f
                LEFT JOIN likes l1 ON f.id = l1.film_id
                LEFT JOIN likes l2 ON f.id = l2.film_id
                WHERE l1.user_id = ? AND l2.user_id = ? AND l1.film_id = l2.film_id
                GROUP BY l1.film_id
                ORDER BY COUNT(l1.user_id) DESC
                """;
        return findMany(findCommonFilmsQuery, userId, friendId);
    }

    @Override
    public Collection<Film> getRecommendedFilms(Long id) {
        String findRecommendedFilmsQuery = """
                SELECT f.*
                FROM films f
                         LEFT JOIN likes l ON f.id = l.film_id
                         RIGHT JOIN (SELECT l.user_id, COUNT(l.film_id)
                                     FROM likes l
                                              RIGHT JOIN (SELECT film_id
                                                          FROM likes
                                                          WHERE user_id = ?) AS ul ON l.film_id = ul.film_id
                                     WHERE l.user_id != ?
                                     GROUP BY l.user_id
                                     ORDER BY COUNT(l.film_id) DESC
                                     LIMIT 1) AS ml ON ml.user_id = l.user_id
                WHERE f.id NOT IN (SELECT film_id FROM likes WHERE user_id = ?)
                """;
        return findMany(findRecommendedFilmsQuery, id, id, id);
    }
}
