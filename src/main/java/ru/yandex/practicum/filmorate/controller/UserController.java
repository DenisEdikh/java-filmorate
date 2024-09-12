package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FilmService filmService;
    private final EventService eventService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Начинаем получение всех пользователей");
        final Collection<User> users = userService.getAllUsers();
        log.info("Получены все пользователи");
        return users;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Начинаем получение пользователя с id = {}", id);
        final User user = userService.getUserById(id);
        log.info("Получен пользователь с id = {}", id);
        return user;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Начинаем добавление пользователя");
        final User savedUser = userService.create(user);
        log.info("Закончено добавление пользователя");
        return savedUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Начинаем обновление пользователя");
        final User savedUser = userService.update(newUser);
        log.info("Закончено Обновление пользователя");
        return savedUser;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable(value = "userId") Long id) {
        log.info("Начинаем удаление пользователя с id = {}", id);
        userService.deleteUser(id);
        log.info("Закончено удаление пользователя с id = {}", id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(value = "id") Long id,
                          @PathVariable(value = "friendId") Long friendId) {
        log.info("Начинаем добавление в друзья пользователя с id = {} пользователя с id = {}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("Закончено добавление в друзья пользователя с id = {} пользователя с id = {}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable(value = "id") Long id,
                             @PathVariable(value = "friendId") Long friendId) {
        log.info("Начинаем удаление из друзей пользователя с id = {} пользователя с id = {}", id, friendId);
        userService.deleteFriend(id, friendId);
        log.info("Закончено удаление из друзей пользователя с id = {} пользователя с id = {}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsUser(@PathVariable(value = "id") Long id) {
        log.info("Начинаем получение всех друзей пользователя с id = {}", id);
        final Collection<User> friendsUser = userService.getFriendsUser(id);
        log.info("Закончено получение всех друзей пользователя с id = {}", id);
        return friendsUser;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable(value = "id") Long id,
                                             @PathVariable(value = "otherId") Long otherId) {
        log.info("Начинаем получение общих друзей пользователей с id = {}, {}", id, otherId);
        final Collection<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Закончено получение общих друзей пользователей с id = {}, {}", id, otherId);
        return commonFriends;
    }

    @GetMapping("/{id}/recommendations")
    public Collection<Film> getRecommendedFilms(@PathVariable(value = "id") Long id) {
        log.info("Начинаем получение рекомендованных фильмов для пользователя с id = {}", id);
        final Collection<Film> recommendedFilms = filmService.getRecommendedFilms(id);
        log.info("Закончено получение рекомендованных фильмов для пользователя с id = {}", id);
        return recommendedFilms;
    }

    @GetMapping("/{id}/feed")
    public Collection<Event> getEventsByUserId(@PathVariable(value = "id") Long id) {
        log.info("Начинаем получение событий для пользователя с id = {}", id);
        final Collection<Event> events = eventService.getEventsByUserId(id);
        log.info("Закончено получение событий для пользователя с id = {}", id);
        return events;
    }
}
