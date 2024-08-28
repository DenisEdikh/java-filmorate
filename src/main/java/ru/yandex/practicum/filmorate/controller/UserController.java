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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        final Collection<User> users = userService.getAllUsers();
        log.info("Выгружены все пользователи");
        return users;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        final User user = userService.getUserById(id);
        log.info("Возвращен пользователь с id = {}", id);
        return user;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable(value = "userId") Long id) {
        userService.deleteUser(id);
        log.info("Удален пользователь с id = {}", id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(value = "id") Long id,
                          @PathVariable(value = "friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable(value = "id") Long id,
                             @PathVariable(value = "friendId") Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsUser(@PathVariable(value = "id") Long id) {
        final Collection<User> friendsUser = userService.getFriendsUser(id);
        log.info("Выгружены все друзья пользователя с id = {}", id);
        return friendsUser;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable(value = "id") Long id,
                                             @PathVariable(value = "otherId") Long otherId) {
        final Collection<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Выгружены общие друзья пользователей с id = {}, {}", id, otherId);
        return commonFriends;
    }
}
