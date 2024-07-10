package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public boolean addLike(Long id, Long userId) {
        final Film film = filmStorage.getFilmById(id);
        userStorage.getUserById(userId);

        film.getLikes().add(userId);
        return true;
    }

    public boolean deleteLike(Long id, Long userId) {
        final Film film = filmStorage.getFilmById(id);
        userStorage.getUserById(userId);

        film.getLikes().remove(id);
        return true;
    }

    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getAll().stream()
                .sorted((film0, film1) -> Long.compare(film1.getLikes().size(), film0.getLikes().size()))
                .limit(count)
                .toList();
    }
}
