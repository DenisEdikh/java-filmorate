package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    public Collection<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public Genre getGenreById(Integer genreId) {
        return genreDbStorage.getGenreById(genreId).orElseThrow(() -> {
            log.warn("Жанр c id = {} не найден", genreId);
            return new NotFoundException(String.format("Жанр с id = %d не найден", genreId));
        });
    }
}
