package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        log.info("Начинаем получение всех жанров");
        final Collection<Genre> genres = genreService.getAllGenres();
        log.info("Закончено получение всех жанров");
        return genres;
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable(value = "id") Integer id) {
        log.info("Начинаем получение жанра по id = {}", id);
        final Genre genre = genreService.getGenreById(id);
        log.info("Начинаем получение жанра по id = {}", id);
        return genre;
    }
}
