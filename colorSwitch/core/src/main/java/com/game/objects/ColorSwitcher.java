package com.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class ColorSwitcher {

    private float x, y;
    private float radius;
    private boolean active;

    private Color[] colors = {
        Color.PINK,
        Color.CYAN,
        Color.YELLOW,
        Color.PURPLE
    };

    private Color[] preferredColors;

    private Circle bounds;

    public ColorSwitcher(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.active = true;
        bounds = new Circle(x, y, radius);
    }

    public ColorSwitcher(float x, float y, float radius, Color[] preferredColors) {
        this(x, y, radius);
        this.preferredColors = preferredColors;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public boolean collidesWith(float ballX, float ballY, float ballRadius) {
        float dist = (float) Math.sqrt((ballX - x) * (ballX - x) + (ballY - y) * (ballY - y));
        return dist < (radius + ballRadius);
    }

    public Color getRandomColor() {
        if (preferredColors != null && preferredColors.length > 0) {
            return preferredColors[MathUtils.random(preferredColors.length - 1)];
        }
        return colors[MathUtils.random(colors.length - 1)];
    }


    public void render(ShapeRenderer sr) {
        if (!active) return;

        float arcAngle = 90f;
        float startAngle = 0f;
        float arcRadius = radius;

        for (Color c : colors) {
            sr.setColor(c);
            sr.arc(x, y, arcRadius, startAngle, arcAngle);
            startAngle += arcAngle;
        }
    }
}
