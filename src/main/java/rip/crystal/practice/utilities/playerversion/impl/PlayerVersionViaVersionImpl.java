package rip.crystal.practice.utilities.playerversion.impl;

import org.bukkit.entity.Player;
import rip.crystal.practice.utilities.playerversion.IPlayerVersion;
import rip.crystal.practice.utilities.playerversion.PlayerVersion;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * ViaVersion moved its API from {@code us.myles.ViaVersion} (3.x and older) to
 * {@code com.viaversion.viaversion} (4.x and newer). Binding to either package directly
 * makes the plugin fail on the other, so both are resolved reflectively at startup.
 */
public class PlayerVersionViaVersionImpl implements IPlayerVersion {

    private static final String[] VIA_CLASSES = {
        "com.viaversion.viaversion.api.Via",
        "us.myles.ViaVersion.api.Via"
    };

    private final Object viaApi;
    private final Method getPlayerVersion;

    public PlayerVersionViaVersionImpl() {
        Object api = null;
        Method method = null;

        for (String className : VIA_CLASSES) {
            try {
                Class<?> viaClass = Class.forName(className);
                api = viaClass.getMethod("getAPI").invoke(null);
                method = api.getClass().getMethod("getPlayerVersion", UUID.class);
                method.setAccessible(true);
                break;
            } catch (Throwable ignored) {
                api = null;
                method = null;
            }
        }

        this.viaApi = api;
        this.getPlayerVersion = method;
    }

    /**
     * @return true if a supported ViaVersion API was found, so the handler can fall back to
     * ProtocolLib instead of failing on every lookup.
     */
    public boolean isAvailable() {
        return viaApi != null && getPlayerVersion != null;
    }

    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        if (!isAvailable()) return PlayerVersion.v1_8;

        try {
            Object raw = getPlayerVersion.invoke(viaApi, player.getUniqueId());
            return PlayerVersion.getVersionFromRaw(((Number) raw).intValue());
        } catch (Throwable ignored) {
            return PlayerVersion.v1_8;
        }
    }
}
