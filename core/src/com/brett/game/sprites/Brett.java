package com.brett.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Brett {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 100;

    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Sound flap;

    private Texture texture;

    public Brett(int x, int y) {
        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);

        texture = new Texture("brettfacesm.png");
        bounds = new Rectangle(x, y, (float)texture.getWidth() / 3, texture.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("flap.mp3"));
    }

    public void update(float dt) {
        // add velocity to position. velocity is scaled by delta time
        if (position.y > 0) {
            velocity.add(0, GRAVITY, 0);
        }

        velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        // forward movement is scaled by delta time
        position.add(MOVEMENT * dt, velocity.y, 0);

        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public void jump() {
        velocity.y = 350;
        flap.play(1f);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
        flap.dispose();
    }
}
