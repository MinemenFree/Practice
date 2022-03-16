package rip.crystal.practice.player.profile.weight;

import java.util.UUID;

public class Weight {
    public final UUID uuid;
    public final int integer;
    public String format;

    public UUID getUuid() {
        return this.uuid;
    }

    public int getInteger() {
        return this.integer;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String string) {
        this.format = string;
    }

    public Weight(UUID uuid, int n) {
        this.uuid = uuid;
        this.integer = n;
    }
}
