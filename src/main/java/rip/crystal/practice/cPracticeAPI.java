package rip.crystal.practice;

import rip.crystal.practice.knockback.Knockback;
import rip.crystal.practice.knockback.KnockbackProfiler;

public class cPracticeAPI {

    public static void setKnockbackProfile(KnockbackProfiler profile) {
        Knockback.setKnockbackProfiler(profile);
    }
}
