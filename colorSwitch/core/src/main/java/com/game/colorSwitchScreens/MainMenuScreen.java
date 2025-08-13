package com.game.colorSwitchScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.colorSwitch.colorSwitch;
import com.game.objects.CircleObstacle;
import com.game.sound.*;

public class MainMenuScreen implements Screen {

    private final colorSwitch game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;

    private static final float VIRTUAL_WIDTH = 800f;
    private static final float VIRTUAL_HEIGHT = 720f;

    private CircleObstacle bigCircle;
    private CircleObstacle smallCircle;

    public MainMenuScreen(colorSwitch game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);

        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        shapeRenderer = new ShapeRenderer();

        float rBig = 90f;
        float rSmall = 90f;
        float centerY = VIRTUAL_HEIGHT - 250f;

        float centerXBig = (VIRTUAL_WIDTH / 2f) - rBig;
        float centerXSmall = (VIRTUAL_WIDTH / 2f) + rSmall; =

        bigCircle = new CircleObstacle(centerXBig, centerY, rBig, 12);
        smallCircle = new CircleObstacle(centerXSmall, centerY, rSmall, 12);


        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(30);
        stage.addActor(table);

        // Title label centered, bigger font
        Label titleLabel = new Label("COLOR SWITCH", skin);
        titleLabel.setColor(Color.WHITE);
        titleLabel.setFontScale(2.5f);

        table.add(titleLabel).padBottom(350).center().row();

        // Buttons vertically below circles
        TextButton playButton = new TextButton("Play", skin);
        TextButton gameInstructionsBtn = new TextButton("Game Instructions", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        playButton.getLabel().setFontScale(1.5f);
        gameInstructionsBtn.getLabel().setFontScale(1.5f);
        exitButton.getLabel().setFontScale(1.5f);

        table.add(playButton).width(250).height(50).padBottom(20).center().row();
        table.add(gameInstructionsBtn).width(250).height(50).padBottom(20).center().row();
        table.add(exitButton).width(250).height(50).center();

        // Button listeners
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                game.setScreen(new com.game.colorSwitchScreens.GameScreen(game));
            }
        });

        gameInstructionsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                game.setScreen(new GameInstructionsScreen(game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                Gdx.app.exit();
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        bigCircle.update(delta);
        smallCircle.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        bigCircle.render(shapeRenderer);
        smallCircle.render(shapeRenderer);
        shapeRenderer.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        shapeRenderer.dispose();
    }
}
