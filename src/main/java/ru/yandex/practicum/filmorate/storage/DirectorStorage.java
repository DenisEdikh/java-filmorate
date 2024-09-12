package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
public class DirectorStorage extends BaseDbStorage<Director> {
    public DirectorStorage(JdbcTemplate jdbcTemplate, RowMapper<Director> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    public Director create(Director director) {
        String insertDirectorQuery = "INSERT INTO directors (name) VALUES (?)";
        final Long id = insert(insertDirectorQuery, director.getName());
        director.setId(id);
        log.info("Добавили директора c id = {}", id);
        return director;
    }

    public Director update(Director director) {
        String updateDirectorQuery = "UPDATE directors SET name = ? WHERE id = ?";
        update(updateDirectorQuery,
                director.getName(),
                director.getId());
        log.info("Обновили пользователя с id = {}", director.getId());
        return director;
    }

    public Collection<Director> getAllDirectors() {
        String findAllQuery = "SELECT * FROM directors";
        return findMany(findAllQuery);
    }

    public Optional<Director> getDirectorById(Long id) {
        String findByIdQuery = "SELECT * FROM directors WHERE id = ?";
        return findOne(findByIdQuery, id);
    }

    public void deleteDirector(Long id) {
        String deleteByIdQuery = "DELETE FROM directors WHERE id = ?";
        update(deleteByIdQuery, id);
        log.info("Удалили директора с id =  {}", id);
    }

    public Collection<Director> getDirectorsByFilmId(Long id) {
        String findDirectorsByFilmIDQuery = """
                SELECT d.* FROM directors d
                LEFT JOIN film_director df ON d.id = df.director_id
                WHERE film_id = ?
                """;
        return findMany(findDirectorsByFilmIDQuery, id);
    }
}
