package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Collection;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director newDirector) {
        log.debug("Начата проверка наличия у директора id");
        checkDirectorId(newDirector);
        log.debug("Закончена проверка наличия у директора id");
        log.debug("Начата проверка наличия директора c id = {} в БД в методе update", newDirector.getId());
        getDirectorById(newDirector.getId());
        log.debug("Закончена проверка наличия директора c id = {} в БД в методе update", newDirector.getId());
        return directorStorage.update(newDirector);
    }

    public Director getDirectorById(Long id) {
        return directorStorage.getDirectorById(id)
                .orElseThrow(() -> {
                    log.warn("Директор c id = {} не найден", id);
                    return new NotFoundException(String.format("Директор с id = %d не найден", id));
                });
    }

    public Collection<Director> getAllDirectors() {
        return directorStorage.getAllDirectors();
    }

    public void deleteDirector(Long id) {
        log.debug("Начата проверка наличия директора c id = {} в БД в методе deleteDirector", id);
        getDirectorById(id);
        log.debug("Закончена проверка наличия директора c id = {} в БД в методе deleteDirector", id);
        directorStorage.deleteDirector(id);
    }

    public Collection<Director> getDirectorsByFilmId(Long id) {
        return directorStorage.getDirectorsByFilmId(id);
    }

    private void checkDirectorId(Director director) {
        if (Objects.isNull(director.getId())) {
            log.warn("У пользователя {} отсутствует id", director);
            throw new ConditionsNotMetException("id должен быть указан");
        }
    }
}
