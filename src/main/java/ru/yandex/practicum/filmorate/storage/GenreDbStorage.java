package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getAllGenres() {
        String findAllQuery = "SELECT * FROM genres";
        return findMany(findAllQuery);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        String findByGenreIdQuery = "SELECT * FROM genres WHERE id = ?";
        return findOne(findByGenreIdQuery, genreId);
    }

    public Collection<Genre> getGenresByFilmId(Long filmId) {
        String findByFilmIdQuery = "SELECT g.* FROM genres g JOIN film_genre fg " +
                "ON g.id = fg.genre_id WHERE film_id = ? ORDER BY g.id";
        return findMany(findByFilmIdQuery, filmId);
    }
}
