package rip.crystal.practice.game.knockback;

import lombok.Getter;
import lombok.Setter;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.knockback.impl.*;

public class Knockback {

    @Getter @Setter public static KnockbackProfiler knockbackProfiler;

    public static void init() {
        switch (cPractice.get().getServer().getName()) {
            case "FoxSpigot":
                knockbackProfiler = new FoxSpigot();
                System.out.print("Hooked into FoxSpigot");
                break;
            case "cSpigot":
                knockbackProfiler = new cSpigot();
                System.out.print("Hooked into cSpigot");
                break;
            case "CarbonSpigot":
                knockbackProfiler = new CarbonSpigot();
                System.out.print("Hooked into CarbonSpigot");
                break;
            case "WindSpigot":
                KnockbackProfiler = new WindSpigot();
                System.out.print("Hooked into WindSpigot");
                break;
            default:
                knockbackProfiler = new Default();
                System.out.print("You don't have a spigot compatible with cPractice's knockback-per-kit system");
                break;
        }
    }
}
