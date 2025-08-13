package com.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Star {
    private float x, y;
    private float radius;
    private boolean collected;

    public Star(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.collected = false;
    }

    public void render(ShapeRenderer renderer) {
        if (collected) return;

        renderer.setColor(Color.GOLD);

        int numPoints = 5;
        float outerRadius = radius;
        float innerRadius = radius / 2.5f;
        float angleStep = (float) Math.PI / numPoints;

        for (int i = 0; i < numPoints * 2; i++) {
            float angle1 = i * angleStep - (float) Math.PI / 2;
            float r1 = (i % 2 == 0) ? outerRadius : innerRadius;
            float x1 = x + (float) Math.cos(angle1) * r1;
            float y1 = y + (float) Math.sin(angle1) * r1;

            float angle2 = (i + 1) * angleStep - (float) Math.PI / 2;
            float r2 = ((i + 1) % 2 == 0) ? outerRadius : innerRadius;
            float x2 = x + (float) Math.cos(angle2) * r2;
            float y2 = y + (float) Math.sin(angle2) * r2;

            renderer.triangle(x, y, x1, y1, x2, y2);
        }
    }

    public boolean collidesWith(float ballX, float ballY, float ballRadius) {
        if (collected) return false;
        float dx = ballX - x;
        float dy = ballY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < ballRadius + radius;
    }

    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
