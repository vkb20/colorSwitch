package com.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Random;

public class GameBall {

    public float x, y, radius, velocityY;
    private final float gravity = -500;
    private Color color;

    private static final Color[] START_COLORS = {
        Color.YELLOW,
        Color.CYAN,
        Color.PINK,
        Color.PURPLE
    };

    public GameBall(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityY = 0;

        this.color = START_COLORS[new Random().nextInt(START_COLORS.length)];
    }

    public void update(float delta) {
        velocityY += gravity * delta;
        y += velocityY * delta;

        if (y < radius) {
            y = radius;
            velocityY = 0;
        }
    }

    public void jump() {
        velocityY = 250;
    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(color);
        renderer.circle(x, y, radius);
    }

    public void setColor(Color newColor) {
        this.color = newColor;
        //this.color = Color.PURPLE;
    }

    public Color getColor() {
        return color;
    }

    public void setPosition(float savedX, float savedY) {
        this.x = savedX;
        this.y = savedY;
    }
}
