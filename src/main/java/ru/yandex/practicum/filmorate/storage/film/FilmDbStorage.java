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
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_FILM_QUERY = "INSERT INTO films (name, " +
            "description, " +
            "release_date, " +
            "duration, " +
            "mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_GENRES_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES(?, ?)";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes(film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String UPDATE_FILM_QUERY = "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
            "WHERE id = ?";
    private static final String FIND_POPULAR_QUERY = "SELECT f.* FROM films f " +
            "JOIN likes l ON f.ID = l.film_id " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(l.user_id) DESC";
    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    @Override
    public Film create(Film film) {
        final Long id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        for (Genre genre : film.getGenres()) {
            update(INSERT_GENRES_QUERY, id, genre.getId());
        }
        log.info("Добавили фильм с id = {}", id);
        return film;
    }

    @Override
    public void createLike(Long filmId, Long userId) {
        update(INSERT_LIKE_QUERY,
                filmId,
                userId);
        log.info("Добавили like фильму с id = {}", filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        update(DELETE_LIKE_QUERY,
                filmId,
                userId);
        log.info("Удалили like фильму с id = {}", filmId);
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_FILM_QUERY,
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
    public Collection<Film> getPopularFilms() {
        return findMany(FIND_POPULAR_QUERY);
    }
}
