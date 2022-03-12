package rip.crystal.practice.visual.tablist.impl.utils;

import lombok.Setter;

@Setter
public class SkinTexture {

    public String SKIN_VALUE, SKIN_SIGNATURE;

    public SkinTexture(String skinValue, String skinSig) {
        this.SKIN_VALUE = skinValue;
        this.SKIN_SIGNATURE = skinSig;
    }

    @Override
    public String toString() {
        return SKIN_SIGNATURE + SKIN_VALUE;
    }
}
