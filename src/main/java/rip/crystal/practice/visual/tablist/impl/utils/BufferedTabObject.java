package rip.crystal.practice.visual.tablist.impl.utils;

import rip.crystal.practice.visual.tablist.impl.TabListCommons;
import rip.crystal.practice.visual.tablist.impl.utils.ping.PingValue;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BufferedTabObject {

    private TabColumn column = TabColumn.LEFT;
    private Integer ping;
    private int slot = 1;
    private String text = "";
    private SkinTexture skinTexture = TabListCommons.defaultTexture;

    public BufferedTabObject text(String text){
        this.text = text;
        return this;
    }

    public BufferedTabObject skin(SkinTexture skinTexture){
        this.skinTexture = skinTexture;
        return this;
    }

    public BufferedTabObject slot(Integer slot){
        this.slot = slot;
        return this;
    }

    public BufferedTabObject ping(Integer ping){
        this.ping = ping;
        return this;
    }

    public BufferedTabObject ping(PingValue ping) {
        this.ping = ping.getValue();
        return this;
    }

    public BufferedTabObject column(TabColumn tabColumn){
        this.column = tabColumn;
        return this;
    }

}
