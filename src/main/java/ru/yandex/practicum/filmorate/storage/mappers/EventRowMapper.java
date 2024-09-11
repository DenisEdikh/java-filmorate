package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

@Component
public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event();

        event.setTimestamp(rs.getObject("timestamp", Instant.class).toEpochMilli());
        event.setUserId(rs.getLong("user_id"));
        event.setEventType(EventType.fromValue(rs.getInt("event_type")));
        event.setOperation(Operation.fromValue(rs.getInt("operation")));
        event.setEventId(rs.getLong("id"));
        event.setEntityId(rs.getLong("entity_id"));
        return event;
    }
}
