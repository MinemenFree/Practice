package rip.crystal.practice.profile.file;

import rip.crystal.practice.profile.Profile;

public interface IProfile {

    void save(Profile profile);

    void load(Profile profile);
}
