package rip.crystal.practice.essentials.command.staff;
/* 
   Made by cpractice Development Team
   Created on 28.11.2021
*/

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import rip.crystal.practice.api.command.BaseCommand;
import rip.crystal.practice.api.command.Command;
import rip.crystal.practice.api.command.CommandArgs;
import rip.crystal.practice.utilities.chat.CC;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TrollCommand extends BaseCommand {

    @Command(name="troll", permission = "cpractice.troll")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        if(args.length == 0) {
            player.sendMessage(CC.translate("&9Usage: /troll (player)"));
        }
        if (args.length > 0) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0].toString());
            if (p.isOnline()) {
                Player enviar = Bukkit.getPlayer(args[0].toString());
                String path = Bukkit.getServer().getClass().getPackage().getName();
                String version = path.substring(path.lastIndexOf(".") + 1, path.length());
                try {
                    Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                    Class<?> PacketPlayOutGameStateChange = Class.forName("net.minecraft.server." + version + ".PacketPlayOutGameStateChange");
                    Class<?> Packet = Class.forName("net.minecraft.server." + version + ".Packet");
                    Constructor<?> playOutConstructor = PacketPlayOutGameStateChange.getConstructor(Integer.TYPE, Float.TYPE);
                    Object packet = playOutConstructor.newInstance(5, 0);
                    Object craftPlayerObject = craftPlayer.cast(enviar);
                    Method getHandleMethod = craftPlayer.getMethod("getHandle", new Class[0]);
                    Object handle = getHandleMethod.invoke(craftPlayerObject, new Object[0]);
                    Object pc = handle.getClass().getField("playerConnection").get(handle);
                    Method sendPacketMethod = pc.getClass().getMethod("sendPacket", Packet);
                    sendPacketMethod.invoke(pc, packet);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                player.sendMessage(CC.translate("&9" + enviar.getName() + " &fgot trolled!"));
            }
        }
    }
}
