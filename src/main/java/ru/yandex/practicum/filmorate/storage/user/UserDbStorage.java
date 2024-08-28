package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Component
@Slf4j
@Qualifier("userDbStorage")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User create(User user) {
        String insertUserQuery = "INSERT INTO users (name, login, email, birthday) VALUES(?, ?, ?, ?)";
        final Long id = insert(insertUserQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()));
        user.setId(id);
        log.info("Добавили пользователя c id = {}", id);
        return user;
    }

    @Override
    public User update(User user) {
        String updateUserQuery = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
        update(updateUserQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        log.info("Обновили пользователя с id =  {}", user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        String deleteUserQuery = "DELETE FROM users WHERE id = ?";
        update(deleteUserQuery, id);
        log.info("Удалили пользователя с id =  {}", id);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        String findByIdQuery = "SELECT * FROM users WHERE id = ?";
        return findOne(findByIdQuery, userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        String findAllQuery = "SELECT * FROM users";
        return findMany(findAllQuery);
    }

    @Override
    public Collection<User> getUsersByFilmId(Long filmId) {
        String findUsersByFilmIdQuery = "SELECT * FROM users u JOIN likes l ON u.id = l.user_id WHERE l.film_id = ?";
        return findMany(findUsersByFilmIdQuery, filmId);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String insertFriendsQuery = "INSERT INTO friends (user_id, user_friend_id) VALUES(?, ?)";
        update(insertFriendsQuery,
                id,
                friendId);
        log.info("Пользователю с id = {} добавлен друг с id = {}", id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String deleteFriendQuery = "DELETE FROM friends WHERE user_id = ? AND user_friend_id = ?";
        update(deleteFriendQuery, id, friendId);
        log.info("Пользователю с id = {} удален друг с id = {}", id, friendId);
    }

    @Override
    public Collection<User> getFriendsUser(Long userId) {
        String findFriendsByUserIdQuery = "SELECT * FROM users u WHERE u.id IN (" +
                "SELECT f.user_friend_id FROM friends f WHERE f.user_id = ?)";
        return findMany(findFriendsByUserIdQuery, userId);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        String findCommonFriendsQuery = "SELECT * FROM USERS u WHERE u.id in (" +
                "SELECT f.USER_FRIEND_ID FROM FRIENDS f " +
                "WHERE f.USER_ID = ? AND f.USER_FRIEND_ID in (" +
                "SELECT f1.USER_FRIEND_ID FROM FRIENDS f1 " +
                "WHERE f1.user_id = ?))";
        return findMany(findCommonFriendsQuery, id, otherId);
    }


}
