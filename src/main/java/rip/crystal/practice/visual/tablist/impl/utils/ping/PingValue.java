package rip.crystal.practice.visual.tablist.impl.utils.ping;

import lombok.Getter;

public enum PingValue {

    NO_CONNECTION(-1),
    ONE_BAR(1000),
    TWO_BARS(999),
    THREE_BARS(599),
    FOUR_BARS(299),
    FULL_BARS(149);

    @Getter private final int value;

    PingValue(int value) {
        this.value = value;
    }
}
