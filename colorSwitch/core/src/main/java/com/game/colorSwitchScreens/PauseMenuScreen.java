package com.game.colorSwitchScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.colorSwitch.colorSwitch;
import com.game.sound.SoundManager;

public class PauseMenuScreen implements Screen {

    private final colorSwitch game;
    private final GameScreen previousGame; // typed
    private Stage stage;
    private Skin skin;

    public PauseMenuScreen(final colorSwitch game, final GameScreen previousGame) {
        this.game = game;
        this.previousGame = previousGame;

        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        TextButton continueBtn = new TextButton("Continue", skin);
        TextButton restartBtn = new TextButton("Restart", skin);
        TextButton exitBtn = new TextButton("Exit to Menu", skin);

        continueBtn.getLabel().setFontScale(1.5f);
        restartBtn.getLabel().setFontScale(1.5f);
        exitBtn.getLabel().setFontScale(1.5f);

        table.add(continueBtn).width(250).height(50).padBottom(20).center().row();
        table.add(restartBtn).width(250).height(50).padBottom(20).center().row();
        table.add(exitBtn).width(250).height(50).center();

        stage.clear();
        stage.addActor(table);

        continueBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // switch back to the same game instance
                SoundManager.playButtonSound();
                game.setScreen(previousGame);
            }
        });

        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                previousGame.dispose();
                game.setScreen(new GameScreen(game));
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                previousGame.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(delta, 1/30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        if (skin != null) skin.dispose();
    }
}
