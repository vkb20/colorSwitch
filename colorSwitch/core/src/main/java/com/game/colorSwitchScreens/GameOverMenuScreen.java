package com.game.colorSwitchScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.colorSwitch.colorSwitch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import com.game.sound.*;

public class GameOverMenuScreen implements Screen {
    private final colorSwitch game;
    private final GameScreen gameScreen;
    private Stage stage;

    public GameOverMenuScreen(final colorSwitch game, final GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label gameOverLabel = new Label("GAME OVER!", skin);
        gameOverLabel.setFontScale(4f); // make it bigger

        TextButton restartButton = new TextButton("Restart", skin);
        TextButton continueButton = new TextButton("Continue", skin);
        TextButton exitButton = new TextButton("Exit to Main Menu", skin);

        restartButton.getLabel().setFontScale(1.5f);
        continueButton.getLabel().setFontScale(1.5f);
        exitButton.getLabel().setFontScale(1.5f);

        table.add(gameOverLabel).pad(20).row();
        table.add(restartButton).width(250).height(50).padBottom(20).center().row();
        table.add(continueButton).width(250).height(50).padBottom(20).center().row();
        table.add(exitButton).width(250).height(50).center();

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                game.setScreen(new GameScreen(game));
            }
        });

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                if (gameScreen.getScore() >= 10) {
                    gameScreen.setScore(gameScreen.getScore() - 10);
                    gameScreen.resetAfterContinue();
                    game.setScreen(gameScreen);
                } else {
                    continueButton.getLabel().setColor(Color.RED);

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            continueButton.getLabel().setColor(Color.WHITE);
                        }
                    }, 0.5f);

                }
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
