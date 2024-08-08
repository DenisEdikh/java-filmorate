package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User create(User user) {
        return userStorage.create(checkName(user));
    }

    public User update(User user) {
        log.debug("Начата проверка наличия у пользователя id");
        checkUserId(user);
        log.debug("Начата проверка наличия пользователя c id = {} в БД", user.getId());
        checkUser(user.getId());
        return userStorage.update(checkName(user));
    }

    public void addFriend(Long id, Long friendId) {
        log.debug("Начата проверка наличия пользователя c id = {}, {} в методе addFriend", id, friendId);
        checkUser(id);
        checkUser(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        log.debug("Начата проверка наличия пользователей c id = {}, {} в методе deleteFriend", id, friendId);
        checkUser(id);
        checkUser(friendId);
        if (userStorage.getFriendsUser(id).stream()
                .map(User::getId)
                .anyMatch(num -> Objects.equals(num, friendId))) {
            log.debug("У пользователя с id = {} есть друг с id = {}", id, friendId);
            userStorage.deleteFriend(id, friendId);
        }
    }

    public Collection<User> getFriendsUser(Long id) {
        log.debug("Начата проверка наличия пользователя c id = {} в методе getFriendsUser", id);
        checkUser(id);
        return userStorage.getFriendsUser(id);
    }


    public Collection<User> getCommonFriends(Long id, Long otherId) {
        log.debug("Начата проверка наличия пользователя c id = {}, {} в методе getCommonFriends", id, otherId);
        checkUser(id);
        checkUser(otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    // метод проверки наличия имени в юзере при запросе
    private User checkName(User user) {
        if (Objects.isNull(user.getName())) {
            log.debug("У пользователя c id = {} отсутствует имя", user.getId());
            user.setName(user.getLogin());
        }
        return user;
    }

    private void checkUserId(User user) {
        if (Objects.isNull(user.getId())) {
            log.warn("У пользователя {} отсутствует id", user);
            throw new ConditionsNotMetException("id должен быть указан");
        }
    }

    private User checkUser(Long userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь c id = {} не найден", userId);
                    return new NotFoundException(String.format("Пользователь с id = %d не найден", userId));
                });
    }
}