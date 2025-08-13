package com.game.colorSwitch;

import com.badlogic.gdx.Game;
import com.game.colorSwitchScreens.MainMenuScreen;
import com.game.sound.*;

public class colorSwitch extends Game {
    @Override
    public void create() {
        SoundManager.load();
        SoundManager.playBackgroundMusic();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        SoundManager.dispose();
    }

}
