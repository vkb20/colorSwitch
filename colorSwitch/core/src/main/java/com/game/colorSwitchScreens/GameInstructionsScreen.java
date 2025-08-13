package com.game.colorSwitchScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.game.colorSwitch.*;
import com.game.sound.SoundManager;

public class GameInstructionsScreen implements Screen {

    private final colorSwitch game;
    private final Stage stage;
    private final OrthographicCamera camera;

    public GameInstructionsScreen(final colorSwitch game) {
        this.game = game;
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(800, 600, camera));
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/Roboto-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter headingParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        headingParam.size = 30;
        headingParam.color = Color.YELLOW;
        headingParam.borderColor = Color.BLACK;
        headingParam.borderWidth = 2;
        BitmapFont headingFont = generator.generateFont(headingParam);

        FreeTypeFontGenerator.FreeTypeFontParameter tipParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        tipParam.size = 24;
        tipParam.color = Color.SKY;
        tipParam.borderColor = Color.BLACK;
        tipParam.borderWidth = 2;
        BitmapFont tipFont = generator.generateFont(tipParam);

        FreeTypeFontGenerator.FreeTypeFontParameter textParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        textParam.size = 20;
        textParam.color = Color.WHITE;
        textParam.borderColor = Color.BLACK;
        textParam.borderWidth = 1;
        BitmapFont textFont = generator.generateFont(textParam);

        generator.dispose();

        Label.LabelStyle headingStyle = new Label.LabelStyle(headingFont, Color.YELLOW);
        Label.LabelStyle tipStyle = new Label.LabelStyle(tipFont, Color.SKY);
        Label.LabelStyle textStyle = new Label.LabelStyle(textFont, Color.WHITE);

        Label headingLabel = new Label("GAME INSTRUCTIONS", headingStyle);
        headingLabel.setAlignment(Align.left);


        Label instructionsLabel = new Label(
            "1. Tap / Click / SPACE to make the ball jump.\n\n"
                + "2. Pass through obstacles matching the ball color.\n\n"
                + "3. Collect stars to increase the score.\n\n"
                + "4. Press P to pause.\n\n"
                + "5. Use color switchers to change the ball color.\n\n"
                + "6. Avoid touching wrong colors  instant game over!\n\n"
                + "7. Score 10+ to use 'Continue' after game over.",
            textStyle
        );
        instructionsLabel.setWrap(true);
        instructionsLabel.setAlignment(Align.left);

        Label tipLabel = new Label("TIP: Time your jumps carefully!", tipStyle);
        tipLabel.setAlignment(Align.left);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        TextButton backButton = new TextButton("Back to Menu", skin);
        backButton.getLabel().setFontScale(1.2f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(30);

        //table.add(headingLabel).expandX().center().padBottom(30).row();
        table.add(headingLabel).expandX().left().padLeft(100).padBottom(30).row();
        table.add(instructionsLabel).width(700).left().padLeft(100).padBottom(30).row();
        //table.add(tipLabel).expandX().center().padBottom(40).row();
        table.add(tipLabel).expandX().left().padLeft(100).padBottom(30).row();
        table.add(backButton).width(250).height(60);

        stage.addActor(table);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
    }
}
