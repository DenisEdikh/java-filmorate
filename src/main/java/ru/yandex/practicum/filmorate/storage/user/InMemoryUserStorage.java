package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long counterId = 0L;

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id = %d не найден", userId)));
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        if (Objects.isNull(user.getName())) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Добавили пользователя {}", user);
        return user;
    }

    @Override
    public User update(User newUser) {
        if (Objects.isNull(newUser.getId())) {
            log.warn("У пользователя {} отсутствует id", newUser);
            throw new ConditionsNotMetException("id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            if (Objects.isNull(newUser.getName())) {
                oldUser.setName(newUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }
            log.info("Обновили данные пользователя {}", newUser);
            return oldUser;
        } else {
            log.warn("Пользователь c id = {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
    }

    private Long getNextId() {
        return ++counterId;
    }
}
