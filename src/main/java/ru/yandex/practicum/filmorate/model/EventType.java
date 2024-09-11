package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;

@Getter
public enum EventType {
    LIKE(1),
    REVIEW(2),
    FRIEND(3);
    private final int value;

    EventType(int value) {
        this.value = value;
    }

    public static EventType fromValue(int value) {
        for (EventType type : EventType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new ConditionsNotMetException("Некорректный тип event");
    }
}
