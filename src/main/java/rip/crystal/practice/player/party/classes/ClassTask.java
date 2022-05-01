package rip.crystal.practice.player.party.classes;

import org.bukkit.entity.Player;
import rip.crystal.practice.player.party.Party;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.ProfileState;

import java.util.stream.Collectors;

public class ClassTask implements Runnable {

    @Override
    public void run() {
        for (Profile profile : Profile.getProfiles().values()
            .stream()
            .filter(profile -> profile.getPlayer() != null && profile.getPlayer().isOnline())
            .filter(profile -> profile.getParty() != null && profile.getState() == ProfileState.FIGHTING)
            .collect(Collectors.toList())) {

            Player player = profile.getPlayer();
            if (profile.getState() != ProfileState.FIGHTING) continue;
            Party party = profile.getParty();
            if (party == null) continue;
            if (party.getArchers().contains(player.getUniqueId())) {
                if (HCFClass.ARCHER.isApply(player)) {
                    HCFClass.ARCHER.equip(player);
                } else {
                    player.getActivePotionEffects().forEach(potionEffect ->
                        player.removePotionEffect(potionEffect.getType()));
                    HCFClass.classMap.remove(player.getUniqueId());
                }
            } else if (party.getRogues().contains(player.getUniqueId())) {
                if (HCFClass.ROGUE.isApply(player)){
                    HCFClass.ROGUE.equip(player);
                } else {
                    player.getActivePotionEffects().forEach(potionEffect ->
                        player.removePotionEffect(potionEffect.getType()));
                    HCFClass.classMap.remove(player.getUniqueId());
                }
            } else if (party.getBards().contains(player.getUniqueId())) {
                if (HCFClass.BARD.isApply(player)) {
                    HCFClass.BARD.equip(player);
                } else {
                    player.getActivePotionEffects().forEach(potionEffect ->
                        player.removePotionEffect(potionEffect.getType()));
                    HCFClass.classMap.remove(player.getUniqueId());
                }
            }
        }
    }
}
