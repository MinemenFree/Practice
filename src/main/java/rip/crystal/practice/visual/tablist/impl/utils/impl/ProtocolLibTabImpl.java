package rip.crystal.practice.visual.tablist.impl.utils.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.playerversion.PlayerVersion;
import rip.crystal.practice.visual.tablist.impl.GhostlyTablist;
import rip.crystal.practice.visual.tablist.impl.TabListCommons;
import rip.crystal.practice.visual.tablist.impl.utils.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class ProtocolLibTabImpl implements IRubenHelper {

    @Override
    public void recreatePlayer(GhostlyTablist tablist, Player player) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
        PlayerInfoData playerInfoData = new PlayerInfoData(profile, player.spigot().getPing(), EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()), WrappedChatComponent.fromText(player.getName()));
        packet.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));
        sendPacket(tablist.getPlayer(), packet);
    }

    @Override
    public TabEntry createFakePlayer(GhostlyTablist zigguratTablist, String string, TabColumn column, Integer slot, Integer rawSlot) {
        UUID uuid = UUID.randomUUID();
        Player player = zigguratTablist.getPlayer();
        PlayerVersion playerVersion = PlayerUtil.getPlayerVersion(player);

        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        WrappedGameProfile profile = new WrappedGameProfile(uuid, playerVersion != PlayerVersion.v1_7  ? string : LegacyClientUtils.tabEntrys.get(rawSlot - 1) + "");
        PlayerInfoData playerInfoData = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(playerVersion != PlayerVersion.v1_7 ?  "" : profile.getName()));
        if (playerVersion != PlayerVersion.v1_7) {
            playerInfoData.getProfile().getProperties().put("textures", new WrappedSignedProperty("textures", TabListCommons.defaultTexture.SKIN_VALUE, TabListCommons.defaultTexture.SKIN_SIGNATURE));
        }

        packet.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));

        /*try {
            packet.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));
        } catch (Exception e)  {
            e.printStackTrace();
        }*/

        sendPacket(player, packet);
        return new TabEntry(string, uuid, "", zigguratTablist, TabListCommons.defaultTexture, column, slot, rawSlot, 1);
    }

    @Override
    public void destoryFakePlayer(GhostlyTablist zigguratTablist, TabEntry tabEntry, String customName) {
        final Player player = zigguratTablist.getPlayer();
        final PlayerVersion playerVersion = PlayerUtil.getPlayerVersion(player);
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);

        WrappedGameProfile profile = new WrappedGameProfile(tabEntry.getUuid(), playerVersion != PlayerVersion.v1_7  ? tabEntry.getId() : LegacyClientUtils.tabEntrys.get(tabEntry.getRawSlot() - 1) + "");
        PlayerInfoData playerInfoData = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(playerVersion != PlayerVersion.v1_7 ?  "" : profile.getName()));

        packet.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));
        sendPacket(player, packet);
    }

    @Override
    public void updateFakeName(GhostlyTablist zigguratTablist, TabEntry tabEntry, String text) {
        final Player player = zigguratTablist.getPlayer();
        final PlayerVersion playerVersion = PlayerUtil.getPlayerVersion(player);
        String[] newStrings = GhostlyTablist.splitStrings(text);
        if (playerVersion == PlayerVersion.v1_7) {
            Team team = player.getScoreboard().getTeam(LegacyClientUtils.teamNames.get(tabEntry.getRawSlot()-1));
            if (team == null) {
                team = player.getScoreboard().registerNewTeam(LegacyClientUtils.teamNames.get(tabEntry.getRawSlot()-1));
                team.addEntry(LegacyClientUtils.tabEntrys.get(tabEntry.getRawSlot() - 1));
            }
            team.setPrefix(ChatColor.translateAlternateColorCodes('&', newStrings[0]));
            if (newStrings.length > 1) {
                team.setSuffix(ChatColor.translateAlternateColorCodes('&', newStrings[1]));
            } else {
                team.setSuffix("");
            }
        } else {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
            packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME);
            WrappedGameProfile profile = new WrappedGameProfile(
                    tabEntry.getUuid(),
                    tabEntry.getId()
            );
            PlayerInfoData playerInfoData = new PlayerInfoData(
                    profile,
                    1,
                    EnumWrappers.NativeGameMode.SURVIVAL,
                    WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', newStrings.length > 1 ? newStrings[0] + newStrings[1] : newStrings[0]))
            );
            packet.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));
            sendPacket(player, packet);
        }
        tabEntry.setText(text);
    }

    @Override
    public void updateFakeLatency(GhostlyTablist zigguratTablist, TabEntry tabEntry, Integer latency) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.UPDATE_LATENCY);

        WrappedGameProfile profile = new WrappedGameProfile(
                tabEntry.getUuid(),
                tabEntry.getId()
        );

        PlayerInfoData playerInfoData = new PlayerInfoData(
                profile,
                latency,
                EnumWrappers.NativeGameMode.SURVIVAL,
                WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', tabEntry.getText()))
        );

        packet.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));
        sendPacket(zigguratTablist.getPlayer(), packet);
        tabEntry.setLatency(latency);
    }

    @Override
    public void updateFakeSkin(GhostlyTablist zigguratTablist, TabEntry tabEntry, SkinTexture skinTexture) {
        final Player player = zigguratTablist.getPlayer();
        final PlayerVersion playerVersion = PlayerUtil.getPlayerVersion(player);

        WrappedGameProfile profile = new WrappedGameProfile(tabEntry.getUuid(), playerVersion != PlayerVersion.v1_7  ? tabEntry.getId() : LegacyClientUtils.tabEntrys.get(tabEntry.getRawSlot() - 1) + "");
        PlayerInfoData playerInfoData = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(playerVersion != PlayerVersion.v1_7 ?  "" : profile.getName()));
        playerInfoData.getProfile().getProperties().put("texture", new WrappedSignedProperty("textures", skinTexture.SKIN_VALUE, skinTexture.SKIN_SIGNATURE));

        PacketContainer remove = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        remove.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        remove.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));

        PacketContainer add = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        add.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        add.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));

        sendPacket(player, remove);
        sendPacket(player, add);

        tabEntry.setTexture(skinTexture);
    }

    @Override
    public void updateHeaderAndFooter(GhostlyTablist zigguratTablist, String header, String footer) {
        final Player player = zigguratTablist.getPlayer();
        PacketContainer headerAndFooter = new PacketContainer(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        headerAndFooter.getChatComponents().write(0, WrappedChatComponent.fromText(header));
        headerAndFooter.getChatComponents().write(1, WrappedChatComponent.fromText(footer));

        sendPacket(player, headerAndFooter);
    }

    @Override
    public SkinTexture getTexture(Player player) {
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
        Collection<WrappedSignedProperty> property = profile.getProperties().get("textures");
        if (property != null && property.size() > 0) {
            WrappedSignedProperty actual = property.iterator().next();
            return new SkinTexture(actual.getValue(), actual.getSignature());
        }
        return TabListCommons.STEVE_TEXTURE;
    }

    private static void sendPacket(Player player, PacketContainer packetContainer){
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
