package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void testGetMpaById() {
        Optional<Mpa> mpaOptional = mpaDbStorage.getMpaById(1);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> assertThat(mpa)
                        .hasFieldOrPropertyWithValue("id", 1)
                        .hasFieldOrPropertyWithValue("name", "G")
                        .isNotNull());
    }

    @Test
    public void testGetAllMpa() {
        Collection<Mpa> mpaes = mpaDbStorage.getAllMpa();

        assertThat(mpaes)
                .hasSize(5)
                .flatExtracting(Mpa::getName).containsOnly("PG", "NC-17", "G", "PG-13", "R");
    }
}