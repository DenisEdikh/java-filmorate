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
        final Collection<Director> directors = directorService.getAllDirectors();
        log.info("Возвращены все директора");
        return directors;
    }

    @GetMapping("{id}")
    public Director getDirectorById(@PathVariable(value = "id") Long id) {
        final Director director = directorService.getDirectorById(id);
        log.info("Возвращен директор с id = {}", id);
        return director;
    }

    @PostMapping
    public Director createDirector(@Valid @RequestBody Director director) {
        return directorService.create(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director newDirector) {
        return directorService.update(newDirector);
    }

    @DeleteMapping("{id}")
    public void deleteDirector(@PathVariable(value = "id") Long id) {
        directorService.deleteDirector(id);
    }

}
