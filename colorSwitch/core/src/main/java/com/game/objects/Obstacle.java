package com.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Obstacle {
    protected float x, y;
    protected float rotation;
    protected float rotationSpeed;

    public Obstacle(float x, float y, float rotationSpeed) {
        this.x = x;
        this.y = y;
        this.rotationSpeed = rotationSpeed;
        this.rotation = 0;
    }

    public void update(float delta) {
        rotation += rotationSpeed * delta;
        if (rotation > 360) rotation -= 360;
    }

    public abstract void render(ShapeRenderer renderer);

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public abstract Color getCollidingColor(GameBall ball);
}
