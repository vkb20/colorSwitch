package com.game.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    public static Music backgroundMusic;
    public static Sound buttonPressSound;
    public static Sound ballJumpSound;
    public static Sound gameOverSound;

    public static void load() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));
        buttonPressSound = Gdx.audio.newSound(Gdx.files.internal("sounds/button_press.wav"));
        ballJumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/ball_jump.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/game_over.wav"));

        backgroundMusic.setLooping(true);
    }

    public static void playBackgroundMusic() {
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public static void stopBackgroundMusic() {
        backgroundMusic.stop();
    }

    public static void playButtonSound() {
        buttonPressSound.play(1.0f);
    }

    public static void playBallJumpSound() {
        ballJumpSound.play(1.0f);
    }

    public static void playGameOverSound() {
        gameOverSound.play(1.0f);
    }

    public static void dispose() {
        backgroundMusic.dispose();
        buttonPressSound.dispose();
        ballJumpSound.dispose();
        gameOverSound.dispose();
    }
}
