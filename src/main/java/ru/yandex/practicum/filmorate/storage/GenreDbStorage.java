package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_GENRE_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT g.* FROM genres g JOIN film_genre fg " +
            "ON g.id = fg.genre_id WHERE film_id = ? ORDER BY g.id";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        return findOne(FIND_BY_GENRE_ID_QUERY, genreId);
    }

    public Collection<Genre> getGenresByFilmId(Long filmId) {
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

}
