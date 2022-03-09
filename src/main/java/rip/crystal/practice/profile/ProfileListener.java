package rip.crystal.practice.profile;

import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.clan.Clan;
import rip.crystal.practice.essentials.event.SpawnTeleportEvent;
import rip.crystal.practice.nametags.GxNameTag;
import rip.crystal.practice.profile.hotbar.Hotbar;
import rip.crystal.practice.profile.hotbar.HotbarItem;
import rip.crystal.practice.profile.visibility.VisibilityLogic;
import rip.crystal.practice.utilities.Cooldown;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.Reflection;
import rip.crystal.practice.utilities.TaskUtil;
import rip.crystal.practice.utilities.chat.CC;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;
import java.util.Collections;

public class ProfileListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onSpawnTeleportEvent(SpawnTeleportEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (!profile.isBusy() && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			Hotbar.giveHotbarItems(event.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if(profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.SPECTATING || profile.getState() == ProfileState.STAFF_MODE || profile.getState() == ProfileState.EVENT) {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE || !event.getPlayer().isOp()) {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(false);
		}
		/*if (profile.getState() != ProfileState.FIGHTING || profile.getState() != ProfileState.FFA) {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE || !event.getPlayer().isOp()) {
				event.setCancelled(true);
			}
		}*/
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (profile.getState() != ProfileState.FIGHTING) {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE || !event.getPlayer().isOp()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
		Profile profile = Profile.get(event.getPlayer().getUniqueId());

		if (profile.getState() == ProfileState.LOBBY) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Profile profile = Profile.get(event.getEntity().getUniqueId());

			if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
				event.setCancelled(true);

				if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
					cPractice.get().getEssentials().teleportToSpawn((Player) event.getEntity());
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			HotbarItem hotbarItem = Hotbar.fromItemStack(event.getItem());

			if (hotbarItem != null) {
				if (hotbarItem.getCommand() != null) {
					event.setCancelled(true);
					player.chat("/" + hotbarItem.getCommand());
				}
			}
		}
		//event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLoginEvent(PlayerLoginEvent event) {
		if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			if (Profile.getProfiles().get(event.getPlayer().getUniqueId()) == null) {
				Profile profile = new Profile(event.getPlayer().getUniqueId());
				try {
					TaskUtil.runAsync(profile::load);
					Profile.getProfiles().put(event.getPlayer().getUniqueId(), profile);
				} catch (Exception e) {
					event.disallow(
							PlayerLoginEvent.Result.KICK_OTHER,
							CC.translate(cPractice.get().getLangConfig().getString("GLOBAL_MESSAGES.FAILED_LOAD_PROFILE")));
					throw new IllegalArgumentException("Player Profile could not be created, contact the plugin author");
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();

		new MessageFormat(Locale.JOIN_MESSAGES
				.format(Profile.get(player.getUniqueId()).getLocale()))
				.send(event.getPlayer());

		PacketPlayOutScoreboardTeam a = new PacketPlayOutScoreboardTeam();
		Reflection.getField(a.getClass(), "a", String.class).set(a, "cPractice");
		Reflection.getField(a.getClass(), "h", int.class).set(a, 3);
		Reflection.getField(a.getClass(), "b", String.class).set(a, "cPractice");
		Reflection.getField(a.getClass(), "f", int.class).set(a, -1);
		Reflection.getField(a.getClass(), "g", Collection.class).set(a, Collections.singletonList(player.getName()));

		for (Player other : Bukkit.getOnlinePlayers()) {
			TaskUtil.runAsync(() -> ((CraftPlayer) other).getHandle().playerConnection.sendPacket(a));
		}

		Profile profile = Profile.get(player.getUniqueId());

//		if (cPractice.get().getRankManager().getRank() instanceof LuckPerms) {
//			Weight weight = new Weight(
//					player.getUniqueId(),
//					Objects.requireNonNull(LuckPermsProvider.get().getGroupManager().getGroup(
//							Objects.requireNonNull(Objects.requireNonNull(LuckPermsProvider.get().getUserManager()
//									.getUser(player.getUniqueId())).getCachedData().getMetaData().getPrimaryGroup()))).getWeight().orElse(0));
//			weight.setFormat(profile.getColor() + player.getName());
//			profile.setWeight(weight);
//		}
//		else if (cPractice.get().getRankManager().getRankSystem().equals("AquaCore")) {
//			Weight weight = new Weight(player.getUniqueId(), AquaCoreAPI.INSTANCE.getGlobalPlayer(player.getUniqueId()).getRankWeight());
//			weight.setFormat(profile.getColor() + player.getName());
//			profile.setWeight(weight);
//			TabAdapter.getRanks().add(weight);
//		}

		profile.setName(player.getName());
		profile.setOnline(true);
		profile.setFishHit(0);
		profile.setState(ProfileState.LOBBY);
		profile.setMatch(null);
		profile.setEnderpearlCooldown(new Cooldown(0));
		profile.setSelectedKit(null);

		if (cPractice.get().getColoredRanksConfig().getConfiguration().contains("groups." + cPractice.get().getRankManager().getRank().getName(player.getUniqueId()))) {
			profile.setColor(cPractice.get().getColoredRanksConfig().getString("groups." + cPractice.get().getRankManager().getRank().getName(player.getUniqueId())));
		} else {
			profile.setColor("&r");
			throw new IllegalArgumentException("The colored rank of " + player.getName() + " doesn't exist");
		}

		cPractice.get().getEssentials().teleportToSpawn(player);
		Hotbar.giveHotbarItems(player);

		if(player.hasPermission("cpractice.fly")) {
			player.setAllowFlight(true);
			player.setFlying(true);
		}

		if (profile.getClan() == null) {
			if (Clan.getByPlayer(player) != null) {
				profile.setClan(Clan.getByPlayer(player));
			}
		}

		TaskUtil.runAsync(()-> {
			if (GxNameTag.isInitiated()) {
				player.setMetadata("sl-LoggedIn", new FixedMetadataValue(cPractice.get(), true));
				GxNameTag.initiatePlayer(player);
				GxNameTag.reloadPlayer(player);
				GxNameTag.reloadOthersFor(player);
			}
		});

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			VisibilityLogic.handle(player, otherPlayer);
			VisibilityLogic.handle(otherPlayer, player);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);

		Profile profile = Profile.get(event.getPlayer().getUniqueId());
		profile.setOnline(false);
		TaskUtil.runAsync(profile::save);

		if (profile.getRematchData() != null) profile.getRematchData().validate();
	}

	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event) {
		if (event.getReason() != null) {
			if (event.getReason().contains("Flying is not enabled")) {
				event.setCancelled(true);
			}
		}
	}
}