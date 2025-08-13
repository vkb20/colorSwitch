package com.game.colorSwitchScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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
import com.game.objects.GameBall;
import com.game.objects.Obstacle;
import com.game.objects.ColorSwitcher;
import com.game.objects.Star;
import com.game.objects.SquareObstacle;
import com.game.objects.CircleObstacle;
import com.game.objects.TriangleObstacle;
import com.game.sound.*;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {

    private final colorSwitch game;
    private ShapeRenderer shapeRenderer;
    private GameBall ball;
    private OrthographicCamera camera;
    private Viewport viewport;

    private static final float VIRTUAL_WIDTH = 800f;
    private static final float VIRTUAL_HEIGHT = 720f;

    private Stage stage;
    private Skin skin;
    private TextButton pauseButton;
    private Label scoreLabel;

    private float groundY;

    private ArrayList<Obstacle> obstacles;
    private float obstacleSpacing = 400f;

    private ArrayList<ColorSwitcher> switchers;
    private ArrayList<Star> stars;

    private int score = 0;

    private boolean gameOverTriggered = false;
    private float gameOverTimer = 0f;
    private final float GAME_OVER_DELAY = 1f;

    private Rectangle bottomRect;

    private Random rand;

    private float savedBallX, savedBallY;
    private Color savedBallColor;
    private ArrayList<Obstacle> savedObstacles;
    private ArrayList<ColorSwitcher> savedSwitchers;
    private ArrayList<Star> savedStars;

    private float resumeInvincibilityTime = 0f;

    public GameScreen(colorSwitch game) {
        this.game = game;

        rand = new Random();

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);

        shapeRenderer = new ShapeRenderer();

        ball = new GameBall(VIRTUAL_WIDTH / 2f, 100, 15);

        float rectHeight = 2 * ball.radius;
        bottomRect = new Rectangle(0, 0, viewport.getWorldWidth(), rectHeight);
        groundY = rectHeight;

        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.top().right();
        table.setFillParent(true);

        pauseButton = new TextButton("Pause", skin);
        pauseButton.getLabel().setFontScale(1.5f);
        table.add(pauseButton).width(250).height(50).padBottom(20).center().row();
        stage.addActor(table);

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playButtonSound();
                game.setScreen(new PauseMenuScreen(game, GameScreen.this));
            }
        });

        scoreLabel = new Label("Score: 0", skin);
        scoreLabel.setFontScale(2.0f);
        scoreLabel.setColor(Color.WHITE);

        Table scoreTable = new Table();
        scoreTable.top().left();
        scoreTable.setFillParent(true);

        scoreTable.add(scoreLabel)
            .width(250)
            .height(50)
            .padBottom(20)
            .padLeft(20)
            .left()
            .row();

        stage.addActor(scoreTable);

        obstacles = new ArrayList<>();
        obstacles.add(createRandomObstacle(400, 400));

        switchers = new ArrayList<>();
        stars = new ArrayList<>();

        if (!(obstacles.get(0) instanceof TriangleObstacle)) {
            stars.add(new Star(400, obstacles.get(0).getY(), 12));
        } else {
            stars.add(new Star(obstacles.get(0).getX(), obstacles.get(0).getY(), 12));
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        score = newScore;
        scoreLabel.setText("Score: " + score);
    }

    public void resetAfterContinue() {
        gameOverTriggered = false;
        gameOverTimer = 0f;

        ball.x = savedBallX;
        ball.y = savedBallY - (ball.radius + 60f);

        camera.position.y = ball.y;

        resumeInvincibilityTime = 1.0f;
    }

    private void saveStateBeforeGameOver() {
        savedBallX = ball.x;
        savedBallY = ball.y;
        savedBallColor = ball.getColor();

        savedObstacles = new ArrayList<>(obstacles);
        savedSwitchers = new ArrayList<>(switchers);
        savedStars = new ArrayList<>(stars);
    }

    private void pauseGame() {
        game.setScreen(new PauseMenuScreen(game, GameScreen.this));
    }


    @Override
    public void show() {
        InputAdapter gameInput = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    ball.jump();
                    SoundManager.playBallJumpSound();
                    return true;
                }
                if (keycode == Input.Keys.P) {
                    pauseGame();
                    SoundManager.playButtonSound();
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                ball.jump();
                SoundManager.playBallJumpSound();
                return true;
            }
        };

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gameInput);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        if (resumeInvincibilityTime > 0f) {
            resumeInvincibilityTime -= delta;
            if (resumeInvincibilityTime < 0f) resumeInvincibilityTime = 0f;
        }

        if (!gameOverTriggered) {
            ball.update(delta);

            if (ball.y > camera.position.y) {
                camera.position.y = ball.y;
            }

            float bottomOfScreen = camera.position.y - viewport.getWorldHeight() / 2;
            if (ball.y + ball.radius < bottomOfScreen) {
                saveStateBeforeGameOver();
                gameOverTriggered = true;
                gameOverTimer = 0f;
                SoundManager.playGameOverSound();
            }

            for (Obstacle o : obstacles) {
                o.update(delta);
            }

            if (resumeInvincibilityTime <= 0f) {
                for (Obstacle o : obstacles) {
                    Color obstacleColor = o.getCollidingColor(ball);
                    if (obstacleColor != null && !obstacleColor.equals(ball.getColor())) {
                        saveStateBeforeGameOver();
                        gameOverTriggered = true;
                        gameOverTimer = 0f;
                        SoundManager.playGameOverSound();
                        break;
                    }
                }
            }

            Obstacle lastObstacle = obstacles.get(obstacles.size() - 1);
            if (camera.position.y + 200 > lastObstacle.getY()) {
                float newY = lastObstacle.getY() + obstacleSpacing;

                Obstacle newObstacle = createRandomObstacle(400, newY);
                obstacles.add(newObstacle);

                float midY = (lastObstacle.getY() + newY) / 2f;

                if (newObstacle instanceof TriangleObstacle) {
                    TriangleObstacle tri = (TriangleObstacle) newObstacle;
                    switchers.add(new ColorSwitcher(400, midY, 20, tri.getColors()));
                } else {
                    switchers.add(new ColorSwitcher(400, midY, 20));
                }

                if (!(newObstacle instanceof TriangleObstacle)) {
                    stars.add(new Star(400, newObstacle.getY(), 12));
                } else {
                    stars.add(new Star(newObstacle.getX(), newObstacle.getY(), 12));
                }
            }

            for (ColorSwitcher s : switchers) {
                if (s.isActive() && s.collidesWith(ball.x, ball.y, ball.radius)) {
                    ball.setColor(s.getRandomColor());
                    s.deactivate();
                }
            }

            for (Star star : stars) {
                if (!star.isCollected() && star.collidesWith(ball.x, ball.y, ball.radius)) {
                    star.collect();
                    score++;
                    scoreLabel.setText("Score: " + score);
                }
            }

        } else {
            gameOverTimer += delta;
            if (gameOverTimer >= GAME_OVER_DELAY) {
                game.setScreen(new GameOverMenuScreen(game, this));
                return;
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(bottomRect.x, bottomRect.y, bottomRect.width, bottomRect.height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        ball.render(shapeRenderer);
        for (Obstacle o : obstacles) {
            if (!(o instanceof CircleObstacle)) {
                o.render(shapeRenderer);
            }
        }
        for (ColorSwitcher s : switchers) {
            s.render(shapeRenderer);
        }
        for (Star star : stars) {
            star.render(shapeRenderer);
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Obstacle o : obstacles) {
            if (o instanceof CircleObstacle) {
                o.render(shapeRenderer);
            }
        }
        shapeRenderer.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
    }

    private Obstacle createRandomObstacle(float x, float y) {
        int r = rand.nextInt(3);
        switch (r) {
            case 0:
                return new SquareObstacle(x, y, 60, 10);
            case 1:
                return new CircleObstacle(x, y, 60, 10);
            case 2:
                return new TriangleObstacle(x, y, 60, 220, 10, ball.getColor());
            default:
                return new SquareObstacle(x, y, 60, 10);
        }
    }
}
