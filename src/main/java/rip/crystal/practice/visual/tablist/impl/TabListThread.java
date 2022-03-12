package rip.crystal.practice.visual.tablist.impl;

public class TabListThread extends Thread {

    private final TabList ziggurat;

    public TabListThread(TabList ziggurat) {
        this.ziggurat = ziggurat;
    }

    @Override
    public void run() {
        while(true) {
            try {
                ziggurat.getTablists().values().forEach(GhostlyTablist::update);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sleep(ziggurat.getTicks() * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
