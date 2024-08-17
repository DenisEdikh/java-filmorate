package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    Collection<User> getUsersByFilmId(Long filmId);

    Optional<User> getUserById(Long userId);

    User create(User user);

    User update(User newUser);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long id, Long friendId);

    Collection<User> getFriendsUser(Long userId);

    Collection<User> getCommonFriends(Long id, Long friendId);
}
