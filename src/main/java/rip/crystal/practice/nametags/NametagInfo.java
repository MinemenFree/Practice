package rip.crystal.practice.nametags;

import rip.crystal.practice.nametags.packets.ScoreboardTeamPacketMod;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class NametagInfo {

    private final String name;
    private final String prefix;
    private final String suffix;
    private final ScoreboardTeamPacketMod teamAddPacket;

    protected NametagInfo(String name, String prefix, String suffix) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;

        teamAddPacket = new ScoreboardTeamPacketMod(name, prefix, suffix, new ArrayList<String>(), 0);
    }
}