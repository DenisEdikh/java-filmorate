package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;

@Getter
public enum Operation {
    REMOVE(1),
    ADD(2),
    UPDATE(3);
    private final int value;

    Operation(int value) {
        this.value = value;
    }

    public static Operation fromValue(int value) {
        for (Operation operation : Operation.values()) {
            if (operation.getValue() == value) {
                return operation;
            }
        }
        throw new ConditionsNotMetException("Некорректный тип Operation");
    }
}