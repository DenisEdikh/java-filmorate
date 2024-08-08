package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotSupportedOperationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@Qualifier("inMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long counterId = 0L;

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавили фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (Objects.isNull(newFilm.getId())) {
            log.warn("У фильма {} отсутствует id", newFilm);
            throw new ConditionsNotMetException("id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Обновили фильм {}", newFilm);
            return oldFilm;
        } else {
            log.warn("Фильм c id = {} не найден", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
    }

    private Long getNextId() {
        return ++counterId;
    }

    @Override
    public void createLike(Long filmId, Long userId) {
        throw new NotSupportedOperationException("Метод в данной реализации интерфейса не поддерживается");
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        throw new NotSupportedOperationException("Метод в данной реализации интерфейса не поддерживается");
    }

    @Override
    public Collection<Film> getPopularFilms() {
        throw new NotSupportedOperationException("Метод в данной реализации интерфейса не поддерживается");
    }
}
