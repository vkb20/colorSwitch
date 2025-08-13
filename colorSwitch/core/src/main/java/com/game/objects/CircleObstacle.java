package com.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CircleObstacle extends Obstacle {

    private float radius;
    protected float thickness;

    private final Color[] colors = new Color[] {
        Color.PINK, Color.CYAN, Color.YELLOW, Color.PURPLE
    };

    public CircleObstacle(float x, float y, float rotationSpeed, float thickness) {
        super(x, y, rotationSpeed);
        this.thickness = thickness;
        this.radius = 90;
    }

    public float getRadius() {
        return radius;
    }


    public void render(ShapeRenderer renderer) {
        renderer.identity();
        renderer.translate(x, y, 0);
        renderer.rotate(0, 0, 1, rotation);

        float outerRadius = radius;
        float innerRadius = radius - thickness;

        for (int i = 0; i < 4; i++) {
            renderer.setColor(colors[i]);
            drawRingSector(renderer, 0, 0, innerRadius, outerRadius, i * 90, 90);
        }

        renderer.identity();
    }

    @Override
    public Color getCollidingColor(GameBall ball) {
        float dx = ball.x - this.x;
        float dy = ball.y - this.y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        float outerRadius = radius;
        float innerRadius = radius - thickness;

        if (dist + ball.radius < innerRadius || dist - ball.radius > outerRadius) {
            return null;
        }

        double rawAngle = Math.toDegrees(Math.atan2(dy, dx));
        double angle = rawAngle - rotation;

        angle = angle % 360;
        if (angle < 0) angle += 360;

        int sector = (int) Math.floor(angle / 90);

        if (sector < 0) sector = 0;
        else if (sector > 3) sector = 3;

        return colors[sector];
    }

    private void drawRingSector(ShapeRenderer renderer, float centerX, float centerY,
                                float innerRadius, float outerRadius,
                                float startAngle, float degrees) {
        int segments = 30;
        float angleStep = degrees / segments;

        for (int i = 0; i < segments; i++) {
            float angle1 = (float) Math.toRadians(startAngle + i * angleStep);
            float angle2 = (float) Math.toRadians(startAngle + (i + 1) * angleStep);

            float x1Outer = centerX + outerRadius * (float) Math.cos(angle1);
            float y1Outer = centerY + outerRadius * (float) Math.sin(angle1);

            float x2Outer = centerX + outerRadius * (float) Math.cos(angle2);
            float y2Outer = centerY + outerRadius * (float) Math.sin(angle2);

            float x1Inner = centerX + innerRadius * (float) Math.cos(angle1);
            float y1Inner = centerY + innerRadius * (float) Math.sin(angle1);

            float x2Inner = centerX + innerRadius * (float) Math.cos(angle2);
            float y2Inner = centerY + innerRadius * (float) Math.sin(angle2);

            renderer.triangle(x1Inner, y1Inner, x1Outer, y1Outer, x2Outer, y2Outer);
            renderer.triangle(x1Inner, y1Inner, x2Outer, y2Outer, x2Inner, y2Inner);
        }
    }
}
