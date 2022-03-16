package rip.crystal.practice.api.rank.impl;

import me.abhi.core.CorePlugin;
import me.abhi.core.profile.CoreProfile;
import me.abhi.core.rank.Rank;

import java.util.UUID;

public class mCore implements rip.crystal.practice.api.rank.Rank {

    public Rank getRank(UUID uuid) {
        CoreProfile coreProfile = CorePlugin.getInstance().getProfileHandler().getCoreProfile(uuid);
        try {
            return coreProfile.getRank();
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getName(UUID uuid) {
        return this.getRank(uuid) == null ? "None" : this.getRank(uuid).getName();
    }

    @Override
    public String getPrefix(UUID uuid) {
        return this.getRank(uuid) == null ? "None" : this.getRank(uuid).getPrefix();
    }

    @Override
    public String getSuffix(UUID uuid) {
        return this.getRank(uuid) == null ? "None" : this.getRank(uuid).getSuffix();
    }

    @Override
    public String getColor(UUID uuid) {
        return this.getRank(uuid) == null ? "None" : this.getRank(uuid).getName();
    }

    @Override
    public int getWeight(UUID uuid) {
        return this.getRank(uuid) == null ? 0 : this.getRank(uuid).getWeight();
    }
}
