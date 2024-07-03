package dev.orisha.event_booker.data.constants;

import lombok.Getter;

@Getter
public enum Type {
    REGULAR(25),
    VIP(15),
    VVIP(5);

    private final int margin;

    Type(int margin) {
        this.margin = margin;
    }
}
