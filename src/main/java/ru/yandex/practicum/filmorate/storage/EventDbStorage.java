package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.util.Collection;

@Repository
@Slf4j
public class EventDbStorage extends BaseDbStorage<Event> {

    public EventDbStorage(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    public void create(Long userId, EventType e, Operation o, Long entityId) {
        String insertEventQuery = """
                INSERT INTO events (user_id, event_type, operation, entity_id)
                VALUES (?, ?, ?, ?)
                """;
        Long eventId = insert(insertEventQuery,
                userId,
                e.getValue(),
                o.getValue(),
                entityId);
        log.info("Добавили событие c id = {}", eventId);
    }

    public Collection<Event> getEventsByUserId(Long userId) {
        String findEventsByUserIdQuery = "SELECT * FROM events WHERE user_id = ?";
        return findMany(findEventsByUserIdQuery, userId);
    }
}

