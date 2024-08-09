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
        final Collection<Mpa> mpaes = mpaService.getAllMpa();
        log.info("Возвращены все рейтинги");
        return mpaes;
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable(value = "id") Integer id) {
        final Mpa mpa = mpaService.getMpaById(id);
        log.info("Возвращен рейтинг с id = {}", id);
        return mpa;
    }
}
