package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.EventDbStorage;
import ru.yandex.practicum.filmorate.storage.ReviewDbStorage;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewDbStorage reviewDbStorage;
    private final EventDbStorage eventDbStorage;
    private final FilmService filmService;
    private final UserService userService;

    public Review create(Review review) {
        log.debug("Начата проверка наличия пользователя, фильма у отзыва с Id = {} в методе create",
                review.getReviewId());
        checkFieldsReview(null, review.getUserId(), review.getFilmId());
        log.debug("Завершена проверка наличия пользователя, фильма у отзыва с Id = {} в методе create",
                review.getReviewId());
        final Review savedReview = reviewDbStorage.create(review);
        eventDbStorage.create(review.getUserId(), EventType.REVIEW, Operation.ADD, review.getReviewId());
        return savedReview;
    }

    public Review update(Review review) {
        log.debug("Начата проверка наличия id у отзыва в методе update");
        checkReviewId(review);
        log.debug("Завершена проверка наличия id у отзыва в методе update");
        log.debug("Начата проверка наличия отзыва, пользователя и фильма у отзыва с Id = {} в методе update",
                review.getReviewId());
        checkFieldsReview(review.getReviewId(), review.getUserId(), review.getFilmId());
        log.debug("Завершена проверка наличия отзыва, пользователя и фильма у отзыва с Id = {} в методе update",
                review.getReviewId());
        final Review savedReview = reviewDbStorage.update(review);
        eventDbStorage.create(review.getUserId(), EventType.REVIEW, Operation.UPDATE, review.getReviewId());
        return savedReview;
    }

    public void delete(Long id) {
        log.debug("Начата проверка наличия отзыва Id = {} в методе delete", id);
        final Review savedReview = getReviewById(id);
        log.debug("Завершена проверка наличия отзыва Id = {} в методе delete", id);
        reviewDbStorage.delete(id);
        eventDbStorage.create(savedReview.getUserId(), EventType.REVIEW, Operation.REMOVE, id);
    }

    public Review getReviewById(Long id) {
        return reviewDbStorage.getReviewById(id).orElseThrow(() -> {
            log.warn("Отзыв c id = {} не найден", id);
            return new NotFoundException(String.format("Отзыв с id = %d не найден", id));
        });
    }

    public Collection<Review> getReviewsByFilmId(Long filmId, Long count) {
        log.debug("Начата проверка наличия фильма у отзыва с Id = {} в методе getReviewsByFilmId", filmId);
        checkFieldsReview(null, null, filmId);
        log.debug("Завершена проверка наличия фильма у отзыва с Id = {} в методе getReviewsByFilmId", filmId);
        return reviewDbStorage.getReviewsByFilmId(filmId, count);
    }

    public void addLike(Long id, Long userId) {
        log.debug("Начата проверка наличия отзыва с Id = {} и пользователя с id = {} в методе addLike", id, userId);
        checkFieldsReview(id, userId, null);
        log.debug("Завершена проверка наличия отзыва с Id = {} и пользователя с id = {} в методе addLike", id, userId);
        reviewDbStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        log.debug("Начата проверка наличия отзыва с Id = {} и пользователя с id = {} в методе deleteLike",
                id,
                userId);
        checkFieldsReview(id, userId, null);
        log.debug("Завершена проверка наличия отзыва с Id = {} и пользователя с id = {} в методе deleteLike",
                id,
                userId);
        reviewDbStorage.deleteLike(id, userId);
    }

    public void addDislike(Long id, Long userId) {
        log.debug("Начата проверка наличия отзыва с Id = {} и пользователя с id = {} в методе addDislike",
                id,
                userId);
        checkFieldsReview(id, userId, null);
        log.debug("Завершена проверка наличия отзыва с Id = {} и пользователя с id = {} в методе addDislike",
                id,
                userId);
        reviewDbStorage.addDislike(id, userId);
    }

    public void deleteDislike(Long id, Long userId) {
        log.debug("Начата проверка наличия отзыва с Id = {} и пользователя с id = {} в методе deleteDislike",
                id,
                userId);
        checkFieldsReview(id, userId, null);
        log.debug("Завершена проверка наличия отзыва с Id = {} и пользователя с id = {} в методе deleteDislike",
                id,
                userId);
        reviewDbStorage.deleteDislike(id, userId);
    }

    private void checkReviewId(Review review) {
        if (Objects.isNull(review.getReviewId())) {
            log.warn("У отзыва {} отсутствует id", review);
            throw new ConditionsNotMetException("id должен быть указан");
        }
    }

    private void checkFieldsReview(Long id, Long userId, Long filmId) {
        if (Objects.nonNull(id)) {
            getReviewById(id);
        }
        if (Objects.nonNull(userId)) {
            userService.getUserById(userId);
        }
        if (Objects.nonNull(filmId)) {
            filmService.getFilmById(filmId);
        }
    }
}

