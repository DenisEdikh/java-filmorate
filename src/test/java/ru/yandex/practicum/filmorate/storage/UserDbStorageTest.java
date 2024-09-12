package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class UserDbStorageTest {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final User user1 = new User();
    private final User user2 = new User();
    private final User user3 = new User();

    @BeforeEach
    public void setUpUser() {
        user1.setName("James Bond");
        user1.setLogin("007");
        user1.setEmail("007@gmail.com");
        user1.setBirthday(LocalDate.of(1920, Month.NOVEMBER, 11));

        user2.setName("Sherlock Holmes");
        user2.setLogin("Holmes");
        user2.setEmail("sh@gmail.com");
        user2.setBirthday(LocalDate.of(1954, Month.JULY, 10));

        user3.setName("John Smith");
        user3.setLogin("Smith");
        user3.setEmail("js@gmail.com");
        user3.setBirthday(LocalDate.of(1900, Month.OCTOBER, 1));
    }

    @Test
    public void testAddUser() {
        User user = userStorage.create(user1);

        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testGetUserById() {
        User user = userStorage.create(user1);
        Optional<User> userDb = userStorage.getUserById(user.getId());

        assertThat(userDb)
                .isPresent()
                .hasValueSatisfying(tempUser -> assertThat(tempUser)
                        .isNotNull()
                        .hasFieldOrPropertyWithValue("id", user1.getId())
                        .hasFieldOrPropertyWithValue("name", user1.getName())
                        .hasFieldOrPropertyWithValue("login", user1.getLogin())
                        .hasFieldOrPropertyWithValue("email", user1.getEmail())
                        .hasFieldOrPropertyWithValue("birthday", user1.getBirthday()));
    }

    @Test
    public void testGetAllUsers() {
        User userDb1 = userStorage.create(user1);
        User userDb2 = userStorage.create(user2);
        User userDb3 = userStorage.create(user3);

        Collection<User> users = userStorage.getAllUsers();

        assertThat(users)
                .hasSize(3);
    }

    @Test
    public void testAddFriend() {
        User userDb1 = userStorage.create(user1);
        User userDb2 = userStorage.create(user2);
        User userDb3 = userStorage.create(user3);

        userStorage.addFriend(userDb1.getId(), userDb2.getId());
        userStorage.addFriend(userDb1.getId(), userDb3.getId());

        Collection<User> friends = userStorage.getFriendsUser(userDb1.getId());

        assertThat(friends)
                .isNotNull()
                .hasSize(2);
    }

    @Test
    public void testDeleteFriend() {
        User userDb1 = userStorage.create(user1);
        User userDb2 = userStorage.create(user2);
        User userDb3 = userStorage.create(user3);

        userStorage.addFriend(userDb1.getId(), userDb2.getId());
        userStorage.addFriend(userDb1.getId(), userDb3.getId());

        userStorage.deleteFriend(userDb1.getId(), userDb2.getId());
        Collection<User> friends = userStorage.getFriendsUser(userDb1.getId());

        assertThat(friends)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    public void testGetCommonFriends() {
        User userDb1 = userStorage.create(user1);
        User userDb2 = userStorage.create(user2);
        User userDb3 = userStorage.create(user3);

        userStorage.addFriend(userDb1.getId(), userDb2.getId());
        userStorage.addFriend(userDb1.getId(), userDb3.getId());
        userStorage.addFriend(userDb2.getId(), userDb3.getId());

        Collection<User> friends = userStorage.getCommonFriends(userDb1.getId(), userDb2.getId());

        assertThat(friends)
                .isNotNull()
                .hasSize(1);
    }

    @Test
    public void testUpdateUser() {
        User userDb1 = userStorage.create(user1);
        user2.setId(userDb1.getId());

        userStorage.update(user2);

        Optional<User> updatedUser = userStorage.getUserById(userDb1.getId());

        assertThat(updatedUser)
                .isPresent()
                .hasValueSatisfying(tempUser -> assertThat(tempUser)
                        .hasFieldOrPropertyWithValue("name", user2.getName())
                        .hasFieldOrPropertyWithValue("login", user2.getLogin())
                        .hasFieldOrPropertyWithValue("email", user2.getEmail())
                        .hasFieldOrPropertyWithValue("birthday", user2.getBirthday())
                );
    }
}
