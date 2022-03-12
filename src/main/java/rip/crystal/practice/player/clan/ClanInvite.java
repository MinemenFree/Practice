package rip.crystal.practice.player.clan;

import lombok.Getter;

import java.util.UUID;

public class ClanInvite {

    @Getter private final UUID sender;
    @Getter private final UUID target;
    private final long timestamp = System.currentTimeMillis();

    public ClanInvite(UUID sender, UUID target){
        this.sender = sender;
        this.target = target;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - this.timestamp >= 60_0000;
    }
}
