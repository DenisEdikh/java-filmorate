package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        log.info("Начато создание отзыва");
        final Review savedReview = reviewService.create(review);
        log.info("Завершено создание отзыва");
        return savedReview;
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("Начато обновление отзыва");
        final Review savedReview = reviewService.update(review);
        log.info("Завершено обновление отзыва");
        return savedReview;
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable(value = "id") Long id) {
        log.info("Начато получение отзыва по id = {}", id);
        final Review review = reviewService.getReviewById(id);
        log.info("Завершено получение отзыва по id = {}", id);
        return review;
    }

    @GetMapping
    public Collection<Review> getReviewsByFilmId(@RequestParam(value = "filmId", required = false) Long filmId,
                                                 @RequestParam(value = "count", defaultValue = "10") Long count) {
        log.info("Начато получение отзывов по filmId = {}", filmId);
        final Collection<Review> reviews = reviewService.getReviewsByFilmId(filmId, count);
        log.info("Завершено получение отзывов по filmId = {}", filmId);
        return reviews;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") @Positive Long id) {
        log.info("Начато удаление отзыва по id = {}", id);
        reviewService.delete(id);
        log.info("Завершено удаление отзыва по id = {}", id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(value = "id") Long id,
                        @PathVariable(value = "userId") Long userId) {
        log.info("Начато добавление \"лайка\" отзыву с id = {} пользователем с userId = {}", id, userId);
        reviewService.addLike(id, userId);
        log.info("Завершено добавление \"лайка\" отзыву с id = {} пользователем с userId = {}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable(value = "id") Long id,
                           @PathVariable(value = "userId") Long userId) {
        log.info("Начато удаление \"лайка\" отзыву с id = {} пользователем с userId = {}", id, userId);
        reviewService.deleteLike(id, userId);
        log.info("Завершено удаление \"лайка\" отзыву с id = {} пользователем с userId = {}", id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable(value = "id") Long id,
                           @PathVariable(value = "userId") Long userId) {
        log.info("Начато добавление \"дизлайка\" отзыву с id = {} пользователем с userId = {}", id, userId);
        reviewService.addDislike(id, userId);
        log.info("Завершено добавление \"дизлайка\" отзыву с id = {} пользователем с userId = {}", id, userId);

    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable(value = "id") Long id,
                              @PathVariable(value = "userId") Long userId) {
        log.info("Начато удаление \"дизлайка\" отзыву с id = {} пользователем с userId = {}", id, userId);
        reviewService.deleteDislike(id, userId);
        log.info("Завершено удаление \"дизлайка\" отзыву с id = {} пользователем с userId = {}", id, userId);
    }
}
