package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
public class ReviewDbStorage extends BaseDbStorage<Review> {
    public ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    public Review create(Review review) {
        String insertReviewQuery = "INSERT INTO reviews (content, is_positive, user_id, film_id) VALUES (?, ?, ?, ?)";
        final Long id = insert(insertReviewQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId());
        review.setReviewId(id);
        log.info("Добавили отзыв c id = {}", id);
        return review;
    }

    public Review update(Review review) {
        String updateReviewQuery = "UPDATE reviews SET content = ?, is_positive = ? WHERE id = ?";
        update(updateReviewQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        log.info("Обновили отзыв c id = {}", review.getReviewId());
        return review;
    }

    public void delete(Long id) {
        String deleteByIdQuery = "DELETE FROM reviews WHERE id = ?";
        update(deleteByIdQuery, id);
        log.info("Удалили отзыв с id =  {}", id);
    }

    public Optional<Review> getReviewById(Long id) {
        String findByIdQuery = """
                SELECT r.*, SUM(u.l_d) as useful FROM reviews r
                LEFT JOIN useful u ON r.id = u.review_id
                WHERE r.id = ?
                GROUP BY r.id
                ORDER BY useful
                """;
        return findOne(findByIdQuery, id);
    }

    public Collection<Review> getReviewsByFilmId(Long filmId, Long count) {
        String findByIdQuery = """
                SELECT r.*, SUM(u.l_d) AS useful FROM reviews r
                LEFT JOIN useful u ON r.id = u.review_id
                WHERE (? IS NULL OR r.film_id = ?)
                GROUP BY r.id
                ORDER BY useful DESC
                LIMIT ?
                """;
        return findMany(findByIdQuery, filmId, filmId, count);
    }

    public void addLike(Long id, Long userId) {
        String addUsefulQuery = "MERGE INTO useful KEY(review_id, user_id) VALUES (?, ?, 1)";
        update(addUsefulQuery, id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        String addUsefulQuery = "DELETE FROM useful WHERE review_id = ? AND user_id = ? AND l_d = 1";
        update(addUsefulQuery, id, userId);
    }

    public void addDislike(Long id, Long userId) {
        String addUsefulQuery = "MERGE INTO useful KEY(review_id, user_id) VALUES (?, ?, -1)";
        update(addUsefulQuery, id, userId);
    }

    public void deleteDislike(Long id, Long userId) {
        String addUsefulQuery = "DELETE FROM useful WHERE review_id = ? AND user_id = ? AND l_d = -1";
        update(addUsefulQuery, id, userId);
    }
}
