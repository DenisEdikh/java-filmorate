package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    public void testGetMpaById() {
        Optional<Genre> genreOptional = genreDbStorage.getGenreById(1);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> assertThat(genre)
                        .hasFieldOrPropertyWithValue("id", 1)
                        .hasFieldOrPropertyWithValue("name", "Комедия")
                        .isNotNull());
    }

    @Test
    public void testGetAllMpa() {
        Collection<Genre> genres = genreDbStorage.getAllGenres();

        assertThat(genres)
                .hasSize(6)
                .flatExtracting(Genre::getName).contains("Комедия",
                        "Драма",
                        "Мультфильм",
                        "Триллер",
                        "Документальный",
                        "Боевик");
    }
}