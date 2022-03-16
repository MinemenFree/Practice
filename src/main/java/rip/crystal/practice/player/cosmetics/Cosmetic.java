package rip.crystal.practice.player.cosmetics;
/* 
   Made by cpractice Development Team
   Created on 30.11.2021
*/

import rip.crystal.practice.player.cosmetics.impl.killeffects.KillEffectType;
import rip.crystal.practice.player.cosmetics.impl.trails.TrailsEffectType;
import rip.crystal.practice.player.profile.Profile;

import java.util.UUID;

public class Cosmetic {

    private int deathEffects = KillEffectType.values().length;

    public String getDeathEffect(UUID uuid) {
        Profile profile = Profile.get(uuid);
        return profile.getKillEffectType().getName();
    }

    public void setDeathEffect(UUID uuid, KillEffectType deathEffect) {
        Profile profile = Profile.get(uuid);
        profile.setKillEffectType(deathEffect);
    }

    public String getTrailEffect(UUID uuid) {
        Profile profile = Profile.get(uuid);
        return profile.getTrailsEffectType().getName();
    }

    public void setTrailEffect(UUID uuid, TrailsEffectType deathEffect) {
        Profile profile = Profile.get(uuid);
        profile.setTrailsEffectType(deathEffect);
    }

    public int getDeathEffects() {
        return deathEffects;
    }
}
