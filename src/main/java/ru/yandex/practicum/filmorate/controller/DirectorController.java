package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
@Slf4j
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Collection<Director> getAllDirectors() {
        log.info("Начинаем получение всех режиссеров");
        final Collection<Director> directors = directorService.getAllDirectors();
        log.info("Закончено получение всех режиссеров");
        return directors;
    }

    @GetMapping("{id}")
    public Director getDirectorById(@PathVariable(value = "id") Long id) {
        log.info("Начинаем получение режиссера с id = {}", id);
        final Director director = directorService.getDirectorById(id);
        log.info("Закончено получение режиссера с id = {}", id);
        return director;
    }

    @PostMapping
    public Director createDirector(@Valid @RequestBody Director director) {
        log.info("Начинаем добавление режиссера");
        final Director savedDirector = directorService.create(director);
        log.info("Закончено добавление режиссера");
        return savedDirector;
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director newDirector) {
        log.info("Начинаем обновление режиссера");
        final Director savedDirector = directorService.update(newDirector);
        log.info("Закончено обновление режиссера");
        return savedDirector;
    }

    @DeleteMapping("{id}")
    public void deleteDirector(@PathVariable(value = "id") Long id) {
        log.info("Начинаем удаление режиссера");
        directorService.deleteDirector(id);
        log.info("Закончено удаление режиссера");
    }
}
