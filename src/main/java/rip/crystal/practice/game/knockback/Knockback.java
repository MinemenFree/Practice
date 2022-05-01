package rip.crystal.practice.game.knockback;

import lombok.Getter;
import lombok.Setter;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.knockback.impl.*;

public class Knockback {

    @Getter @Setter public static KnockbackProfiler knockbackProfiler;

    public static void init() {
        switch (cPractice.get().getServer().getName()) {
            case "GxSpigot":
                knockbackProfiler = new GxSpigot();
                System.out.print("Hooked into GxSpigot");
                break;
            case "FoxSpigot":
                knockbackProfiler = new FoxSpigot();
                System.out.print("Hooked into FoxSpigot");
                break;
            case "cSpigot":
                knockbackProfiler = new cSpigot();
                System.out.print("Hooked into cSpigot");
                break;
            case "InsanePaper":
                knockbackProfiler = new InsanePaperSpigot();
                System.out.print("Hooked into InsanePaper");
                break;
            case "gSpigot":
                knockbackProfiler = new gSpigot();
                System.out.print("Hooked into gSpigot");
                break;
            case "CarbonSpigot":
                knockbackProfiler = new CarbonSpigot();
                System.out.print("Hooked into CarbonSpigot");
                break;
            default:
                knockbackProfiler = new Default();
                System.out.print("You don't have a spigot compatible with cpractice's Knockbacks");
                System.out.print("You don't have a spigot compatible with cpractice's Knockbacks");
                System.out.print("You don't have a spigot compatible with cpractice's Knockbacks");
                break;
        }
    }
}
