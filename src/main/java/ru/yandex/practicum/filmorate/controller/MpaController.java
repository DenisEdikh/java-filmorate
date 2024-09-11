package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> getAllMpa() {
        log.info("Начинаем получение всех рейтингов");
        final Collection<Mpa> mpaes = mpaService.getAllMpa();
        log.info("Закончено получение всех рейтингов");
        return mpaes;
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable(value = "id") Integer id) {
        log.info("Начинаем получение рейтинга по id = {}", id);
        final Mpa mpa = mpaService.getMpaById(id);
        log.info("Закончено получение рейтинга по id = {}", id);
        return mpa;
    }
}
