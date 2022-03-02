package rip.crystal.practice.utilities.playerversion;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PlayerVersion {

    v1_7(4,5),
    v1_8(47),
    v1_9(107, 108, 109, 110),
    v1_10(210),
    v1_11(315, 316),
    v1_12(335, 338, 340),
    v1_13(393, 401, 404);

    private final Integer[] rawVersion;

    PlayerVersion(Integer... rawVersionNumbers) {
        this.rawVersion = rawVersionNumbers;
    }

    public static PlayerVersion getVersionFromRaw(Integer input){
        for (PlayerVersion playerVersion : PlayerVersion.values()) {
            if (Arrays.asList(playerVersion.rawVersion).contains(input)) {
                return playerVersion;
            }
        }
        return v1_8;
    }
}
