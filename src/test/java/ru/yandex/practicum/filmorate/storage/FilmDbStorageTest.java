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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class FilmDbStorageTest {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final Film film1 = new Film();
    private final Film film2 = new Film();
    private final Film film3 = new Film();
    private final Mpa mpa1 = new Mpa();
    private final Mpa mpa2 = new Mpa();
    private final Mpa mpa3 = new Mpa();
    private final Genre genre1 = new Genre();
    private final Genre genre2 = new Genre();
    private final Genre genre3 = new Genre();

    @BeforeEach
    public void setUpFilm() {
        film1.setName("Terminator");
        film1.setDescription("Description");
        film1.setReleaseDate(LocalDate.of(1984, Month.OCTOBER, 11));
        film1.setDuration(90);
        mpa1.setId(4);
        film1.setMpa(mpa1);
        genre1.setId(4);
        film1.addGenres(List.of(genre1));

        film2.setName("Terminator 2");
        film2.setDescription("Description 2");
        film2.setReleaseDate(LocalDate.of(1991, Month.OCTOBER, 11));
        film2.setDuration(125);
        mpa2.setId(4);
        film2.setMpa(mpa2);
        genre2.setId(4);
        film2.addGenres(List.of(genre2));

        film3.setName("HomeAlone");
        film3.setDescription("Description 3");
        film3.setReleaseDate(LocalDate.of(1990, Month.OCTOBER, 11));
        film3.setDuration(100);
        mpa3.setId(1);
        film3.setMpa(mpa3);
        genre3.setId(2);
        film3.addGenres(List.of(genre2, genre3));
    }

    @Test
    public void testAddFilm() {
        Film film = filmStorage.create(film1);

        assertThat(film)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testGetFilmById() {
        Film film = filmStorage.create(film1);

        Optional<Film> filmDb = filmStorage.getFilmById(film.getId());

        assertThat(filmDb)
                .isPresent()
                .hasValueSatisfying(tempFilm -> {
                    assertThat(tempFilm)
                            .hasFieldOrPropertyWithValue("name", film1.getName())
                            .hasFieldOrPropertyWithValue("description", film1.getDescription())
                            .hasFieldOrPropertyWithValue("releaseDate", film1.getReleaseDate())
                            .hasFieldOrPropertyWithValue("duration", film1.getDuration());
                });
    }

    @Test
    public void testGetAllFilms() {
        filmStorage.create(film1);
        filmStorage.create(film2);
        filmStorage.create(film3);

        Collection<Film> films = filmStorage.getAllFilms();

        assertThat(films).hasSize(3);
    }

    @Test
    public void testUpdateFilm() {
        Film film = filmStorage.create(film1);
        film2.setId(film.getId());

        filmStorage.update(film2);

        Optional<Film> updatedFilm = filmStorage.getFilmById(film.getId());

        assertThat(updatedFilm)
                .isPresent()
                .hasValueSatisfying(tempFilm -> {
                    assertThat(tempFilm)
                            .hasFieldOrPropertyWithValue("name", film2.getName())
                            .hasFieldOrPropertyWithValue("description", film2.getDescription())
                            .hasFieldOrPropertyWithValue("releaseDate", film2.getReleaseDate())
                            .hasFieldOrPropertyWithValue("duration", film2.getDuration());
                });
    }
}
