package com.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SquareObstacle extends Obstacle {

    private float radius;
    protected float thickness;

    private final Color[] colors = new Color[] {
        Color.PINK, Color.CYAN, Color.YELLOW, Color.PURPLE
    };

    public SquareObstacle(float x, float y, float rotationSpeed, float thickness) {
        super(x, y, rotationSpeed);
        this.thickness = thickness;
        this.radius = 80;
    }

    @Override
    public void render(ShapeRenderer renderer) {
        float half = radius;

        renderer.identity();
        renderer.translate(x, y, 0);
        renderer.rotate(0, 0, 1, rotation);

        for (int i = 0; i < 4; i++) {
            renderer.setColor(colors[i]);
            switch(i) {
                case 0:
                    renderer.rectLine(-half, half, half, half, thickness);
                    break;
                case 1:
                    renderer.rectLine(half, half, half, -half, thickness);
                    break;
                case 2:
                    renderer.rectLine(half, -half, -half, -half, thickness);
                    break;
                case 3:
                    renderer.rectLine(-half, -half, -half, half, thickness);
                    break;
            }
        }

        renderer.identity();
    }

    @Override
    public Color getCollidingColor(GameBall ball) {
        float dx = ball.x - this.x;
        float dy = ball.y - this.y;

        double rad = Math.toRadians(-rotation);
        float localX = (float)(dx * Math.cos(rad) - dy * Math.sin(rad));
        float localY = (float)(dx * Math.sin(rad) + dy * Math.cos(rad));

        float half = radius;
        float thicknessOffset = thickness / 2f + ball.radius;

        if (localY >= half - thicknessOffset && localY <= half + thicknessOffset && localX >= -half && localX <= half) {
            return colors[0];
        }
        else if (localX >= half - thicknessOffset && localX <= half + thicknessOffset && localY >= -half && localY <= half) {
            return colors[1];
        }
        else if (localY <= -half + thicknessOffset && localY >= -half - thicknessOffset && localX >= -half && localX <= half) {
            return colors[2];
        }
        else if (localX <= -half + thicknessOffset && localX >= -half - thicknessOffset && localY >= -half && localY <= half) {
            return colors[3];
        }

        return null;
    }
}
