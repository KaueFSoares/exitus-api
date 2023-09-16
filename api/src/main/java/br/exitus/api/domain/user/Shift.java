package br.exitus.api.domain.user;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = "name")
public enum Shift {
    MORNING("MORNING", "12:15"),
    AFTERNOON("AFTERNOON", "18:15"),
    NIGHT("NIGHT", "22:00");

    private final String name;
    private final String endTime;

    Shift(String name, String endTime) {
        this.name = name;
        this.endTime = endTime;
    }

}