package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Mpa {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
}
