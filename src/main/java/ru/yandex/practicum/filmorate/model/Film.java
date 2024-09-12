package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validators.DateValidation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
public class Film {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Size(message = "Максимальная длина описания - 200 символов", max = 200)
    private String description;
    @DateValidation
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer duration;
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres;
    private Set<Director> directors;

    public void addGenres(Collection<Genre> genres) {
        if (this.genres == null) {
            this.genres = new LinkedHashSet<>();
        }
        this.genres.addAll(genres);
    }

    public void addDirectors(Collection<Director> directors) {
        if (this.directors == null) {
            this.directors = new LinkedHashSet<>();
        }
        this.directors.addAll(directors);
    }
}
