package com.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Collections;

public class TriangleObstacle extends Obstacle {

    private float sideLength;
    protected float thickness;

    private static final Color[] ALL_COLORS = new Color[] {
        Color.PINK, Color.CYAN, Color.YELLOW, Color.PURPLE
    };

    private final Color[] colors = new Color[3];


    public TriangleObstacle(float x, float y, float rotationSpeed, float sideLength, float thickness, Color ballColor) {
        super(x, y, rotationSpeed);
        this.sideLength = sideLength;
        this.thickness = thickness;

        colors[0] = ballColor;

        ArrayList<Color> remainingColors = new ArrayList<>();
        for (Color c : ALL_COLORS) {
            if (!c.equals(ballColor)) {
                remainingColors.add(c);
            }
        }

        Collections.shuffle(remainingColors);
        colors[1] = remainingColors.get(0);
        colors[2] = remainingColors.get(1);
    }

    @Override
    public void render(ShapeRenderer renderer) {
        renderer.identity();
        renderer.translate(x, y, 0);
        renderer.rotate(0, 0, 1, rotation);

        float halfSide = sideLength / 2f;
        float height = (float) (Math.sqrt(3) * halfSide);

        float x1 = -halfSide, y1 = -height / 3;
        float x2 = halfSide, y2 = -height / 3;
        float x3 = 0, y3 = 2 * height / 3;

        renderer.setColor(colors[0]);
        renderer.rectLine(x1, y1, x2, y2, thickness);

        renderer.setColor(colors[1]);
        renderer.rectLine(x2, y2, x3, y3, thickness);

        renderer.setColor(colors[2]);
        renderer.rectLine(x3, y3, x1, y1, thickness);

        renderer.identity();
    }

    @Override
    public Color getCollidingColor(GameBall ball) {
        float dx = ball.x - this.x;
        float dy = ball.y - this.y;

        double rad = Math.toRadians(-rotation);
        float localX = (float) (dx * Math.cos(rad) - dy * Math.sin(rad));
        float localY = (float) (dx * Math.sin(rad) + dy * Math.cos(rad));

        float halfSide = sideLength / 2f;
        float height = (float) (Math.sqrt(3) * halfSide);

        float[] vx = new float[]{-halfSide, halfSide, 0};
        float[] vy = new float[]{-height / 3, -height / 3, 2 * height / 3};

        for (int i = 0; i < 3; i++) {
            int j = (i + 1) % 3;

            float sx = vx[j] - vx[i];
            float sy = vy[j] - vy[i];

            float bx = localX - vx[i];
            float by = localY - vy[i];

            float proj = (bx * sx + by * sy) / (sx * sx + sy * sy);
            proj = Math.max(0, Math.min(1, proj));

            float closestX = vx[i] + proj * sx;
            float closestY = vy[i] + proj * sy;

            float distX = localX - closestX;
            float distY = localY - closestY;
            float dist = (float) Math.sqrt(distX * distX + distY * distY);

            if (dist <= ball.radius + thickness / 2f) {
                return colors[i];
            }
        }

        return null;
    }

    public Color[] getColors() {
        return colors.clone();
    }
}
