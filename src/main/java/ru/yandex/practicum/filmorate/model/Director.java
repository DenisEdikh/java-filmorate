package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Director {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
}
