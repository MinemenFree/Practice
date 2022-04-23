package rip.crystal.practice;

import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.game.knockback.Knockback;
import rip.crystal.practice.game.knockback.KnockbackProfiler;

public class cPracticeAPI {

    public static cPracticeAPI INSTANCE;

    public static void setKnockbackProfile(KnockbackProfiler profile) {
        Knockback.setKnockbackProfiler(profile);
    }

    public static Kit getKit(String name) {
        return Kit.getByName(name);
    }

    public static Arena getRandomArena(Kit kit) {
        return Arena.getRandomArena(kit);
    }
}
