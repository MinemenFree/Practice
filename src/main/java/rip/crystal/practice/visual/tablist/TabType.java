package rip.crystal.practice.visual.tablist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TabType {

    DEFAULT("Normal"),
    WEIGHT("Weight");

    public final String format;
}
