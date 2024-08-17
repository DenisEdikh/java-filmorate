package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    public Collection<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }

    public Mpa getMpaById(Integer mpaId) {
        return mpaDbStorage.getMpaById(mpaId)
                .orElseThrow(() -> {
                    log.warn("Mps c id = {} не найден", mpaId);
                    return new NotFoundException(String.format("Mpa с id = %d не найден", mpaId));
                });
    }
}
