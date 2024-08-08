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
    private static final String INSERT_USER_QUERY = "INSERT INTO users (name, login, email, birthday) " +
            "VALUES(?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String INSERT_FRIENDS_QUERY = "INSERT INTO friends (user_id, user_friend_id) " +
            "VALUES(?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_USERS_BY_FILM_ID_QUERY = "SELECT * FROM users u JOIN likes l " +
            "ON u.id = l.user_id WHERE l.film_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_FRIENDS_BY_USER_ID_QUERY = "SELECT * FROM users u WHERE u.id IN (" +
            "SELECT f.user_friend_id FROM friends f WHERE f.user_id = ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND user_friend_id = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT * FROM USERS u WHERE u.id in (" +
            "SELECT f.USER_FRIEND_ID FROM FRIENDS f " +
            "WHERE f.USER_ID = ? AND f.USER_FRIEND_ID in (" +
            "SELECT f1.USER_FRIEND_ID FROM FRIENDS f1 " +
            "WHERE f1.user_id = ?))";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User create(User user) {
        final Long id = insert(INSERT_USER_QUERY,
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
        update(UPDATE_USER_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        log.info("Обновили пользователя с id =  {}", user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Collection<User> getUsersByFilmId(Long filmId) {
        return findMany(FIND_USERS_BY_FILM_ID_QUERY, filmId);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        update(INSERT_FRIENDS_QUERY,
                id,
                friendId);
        log.info("Пользователю с id = {} добавлен друг с id = {}", id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        update(DELETE_FRIEND_QUERY, id, friendId);
        log.info("Пользователю с id = {} удален друг с id = {}", id, friendId);
    }

    @Override
    public Collection<User> getFriendsUser(Long userId) {
        return findMany(FIND_FRIENDS_BY_USER_ID_QUERY, userId);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return findMany(FIND_COMMON_FRIENDS_QUERY, id, otherId);
    }
}
