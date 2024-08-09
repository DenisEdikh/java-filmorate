package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
public class MpaDbStorage extends BaseDbStorage<Mpa> {
    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Mpa> getAllMpa() {
        String findAllMpa = "SELECT * FROM mpa";
        return findMany(findAllMpa);
    }

    public Optional<Mpa> getMpaById(Integer mpaId) {
        String findMpaById = "SELECT * FROM mpa WHERE id = ?";
        return findOne(findMpaById, mpaId);
    }
}
