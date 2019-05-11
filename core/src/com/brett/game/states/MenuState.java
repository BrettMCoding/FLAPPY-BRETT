package com.brett.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.brett.game.FlappyDemo;

public class MenuState extends State {
    private Texture background;
    private Texture title;
    private Texture brettface;
    private Sprite brettfaceSprite;
    private Texture playBtn;
    private Texture musicOffBtn;
    private Texture musicOnBtn;
    private Rectangle musicBtnBounds;
    private Preferences prefs;
    private boolean musicOn;


    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        background = new Texture("bg.png");
        title = new Texture("title.png");
        brettface = new Texture("brettface.png");
        brettfaceSprite = new Sprite(brettface);
        brettfaceSprite.setPosition(cam.position.x - brettface.getWidth() / 2, cam.position.y - 125);
        playBtn = new Texture("playbtn.png");
        musicOnBtn = new Texture("musiconbtn.png");
        musicOffBtn = new Texture("musicoffbtn.png");
        musicBtnBounds = new Rectangle(cam.position.x - playBtn.getWidth() / 2, cam.position.y - 190, musicOnBtn.getWidth(), musicOnBtn.getHeight());
        prefs = Gdx.app.getPreferences("fbrprefs");
        musicOn = prefs.getBoolean("music");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 tmp=new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(tmp);
            // texture x is the x position of the texture
            // texture y is the y position of the texture
            if(musicBtnBounds.contains(tmp.x,tmp.y)) {
                if (!prefs.getBoolean("music")) {
                    musicOn = true;
                    prefs.putBoolean("music", true);
                    prefs.flush();
                } else {
                    musicOn = false;
                    prefs.putBoolean("music", false);
                    prefs.flush();
                }
            } else {
                gsm.set(new PlayState(gsm));
            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0,0);
        sb.draw(title, cam.position.x - title.getWidth() / 2, cam.position.y + 40);
        brettfaceSprite.draw(sb);

        sb.draw(playBtn, cam.position.x - playBtn.getWidth() / 2, cam.position.y - 50);

        if (musicOn) {

            brettfaceSprite.rotate(-21f);

            sb.draw(musicOffBtn, cam.position.x - playBtn.getWidth() / 2, cam.position.y - 190);
            } else {
            sb.draw(musicOnBtn, cam.position.x - playBtn.getWidth() / 2, cam.position.y - 190);
        }

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        musicOnBtn.dispose();
        musicOffBtn.dispose();
        brettface.dispose();
    }
}
