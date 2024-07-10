package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long id, Long friendId) {
        final User user = userStorage.getUserById(id);
        final User userFriend = userStorage.getUserById(friendId);

        userFriend.getFriends().add(id);
        user.getFriends().add(friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        final User user = userStorage.getUserById(id);
        final User userFriend = userStorage.getUserById(friendId);

        user.getFriends().remove(friendId);
        userFriend.getFriends().remove(id);
    }

    public List<User> getFriendsUser(Long id) {
        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .toList();
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        final User user = userStorage.getUserById(id);
        final User otherUser = userStorage.getUserById(otherId);

        return otherUser.getFriends().stream()
                .filter(userId -> user.getFriends().contains(userId))
                .map(userStorage::getUserById)
                .toList();
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }
}