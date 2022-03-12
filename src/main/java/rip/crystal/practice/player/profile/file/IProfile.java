package rip.crystal.practice.player.profile.file;

import rip.crystal.practice.player.profile.Profile;

public interface IProfile {

    void save(Profile profile);

    void load(Profile profile);
}
