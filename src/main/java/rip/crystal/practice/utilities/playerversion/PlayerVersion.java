package rip.crystal.practice.utilities.playerversion;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum PlayerVersion {

    v1_7(4, 5),
    v1_8(47),
    v1_9(107, 108, 109, 110),
    v1_10(210),
    v1_11(315, 316),
    v1_12(335, 338, 340),
    v1_13(393, 401, 404),
    v1_14(477, 480, 485, 490, 498),
    v1_15(573, 575, 578),
    v1_16(735, 736, 751, 753, 754),
    v1_17(755, 756),
    v1_18(757, 758),
    v1_19(759, 760, 761, 762),
    v1_20(763, 764, 765, 766),
    v1_21(767, 768, 769, 770, 771, 772);

    /** Protocol number -> version, built once instead of scanning every lookup. */
    private static final Map<Integer, PlayerVersion> BY_PROTOCOL = new HashMap<>();

    static {
        for (PlayerVersion playerVersion : values()) {
            for (Integer raw : playerVersion.rawVersion) {
                BY_PROTOCOL.put(raw, playerVersion);
            }
        }
    }

    private final Integer[] rawVersion;

    PlayerVersion(Integer... rawVersionNumbers) {
        this.rawVersion = rawVersionNumbers;
    }

    /**
     * Unknown protocols (a client newer than this table) fall back to {@link #v1_8}, which
     * keeps them on the modern tablist path - only {@link #v1_7} is rendered differently.
     */
    public static PlayerVersion getVersionFromRaw(Integer input) {
        PlayerVersion found = BY_PROTOCOL.get(input);
        return found == null ? v1_8 : found;
    }
}
