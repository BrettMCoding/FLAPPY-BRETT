package com.brett.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.brett.game.FlappyBrett;

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
        cam.setToOrtho(false, (float)FlappyBrett.WIDTH / 2, (float)FlappyBrett.HEIGHT / 2);

        background = new Texture("bg.png");
        title = new Texture("title.png");
        playBtn = new Texture("playbtn.png");
        musicOnBtn = new Texture("musiconbtn.png");
        musicOffBtn = new Texture("musicoffbtn.png");
        brettface = new Texture("brettface.png");

        brettfaceSprite = new Sprite(brettface);
        brettfaceSprite.setPosition(cam.position.x - (float)brettface.getWidth() / 2, cam.position.y - 125);

        musicBtnBounds = new Rectangle(cam.position.x - (float)playBtn.getWidth() / 2, cam.position.y - 190, musicOnBtn.getWidth(), musicOnBtn.getHeight());

        prefs = Gdx.app.getPreferences("fbrprefs");
        musicOn = prefs.getBoolean("music");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {

            // create a Vector3 and check if we pressed in the Music Button's Bounds
            Vector3 tmp=new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(tmp);
            if(musicBtnBounds.contains(tmp.x,tmp.y)) {

                // if so, change the preference, and the local boolean
                // using a local boolean because (I assume) checking it every frame
                // render is less resource intensive
                if (!prefs.getBoolean("music")) {
                    musicOn = true;
                    prefs.putBoolean("music", true);
                    prefs.flush();
                } else {
                    musicOn = false;
                    prefs.putBoolean("music", false);
                    prefs.flush();
                }
            // if the screen is clicked anywhere else, start the game
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

        sb.draw(title, cam.position.x - (float)title.getWidth() / 2, cam.position.y + 40);
        brettfaceSprite.draw(sb);

        sb.draw(playBtn, cam.position.x - (float)playBtn.getWidth() / 2, cam.position.y - 50);

        drawMusicButton(sb);

        sb.end();
    }

    // change off/on based on boolean
    private void drawMusicButton(SpriteBatch sb) {
        if (musicOn) {
            // SPIN
            brettfaceSprite.rotate(-21f);

            sb.draw(musicOffBtn, cam.position.x - (float)musicOnBtn.getWidth() / 2, cam.position.y - 190);
        } else {
            sb.draw(musicOnBtn, cam.position.x - (float)musicOnBtn.getWidth() / 2, cam.position.y - 190);
        }
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
