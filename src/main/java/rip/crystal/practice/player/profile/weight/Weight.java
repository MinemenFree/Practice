package rip.crystal.practice.player.profile.weight;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
public class Weight {

    public final UUID uuid;
    public final int integer;
    public String format;

}
