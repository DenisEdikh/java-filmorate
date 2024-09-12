package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventDbStorage eventDbStorage;
    private final UserService userService;

    public Collection<Event> getEventsByUserId(Long userId) {
        log.debug("Начата проверка наличия пользователя c id = {} в БД в методе update", userId);
        userService.getUserById(userId);
        log.debug("Закончена проверка наличия пользователя c id = {} в БД в методе update", userId);
        return eventDbStorage.getEventsByUserId(userId);
    }
}
