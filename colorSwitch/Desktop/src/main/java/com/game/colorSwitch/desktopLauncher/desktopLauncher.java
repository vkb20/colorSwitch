package com.game.colorSwitch.desktopLauncher;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.game.colorSwitch.colorSwitch;

public class desktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("colorSwitch");
        config.setWindowedMode(640, 720);
        config.useVsync(true);
        new Lwjgl3Application(new colorSwitch(), config);
    }
}
