package com.brett.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.brett.game.FlappyDemo;
import com.brett.game.sprites.Bird;
import com.brett.game.sprites.Tube;

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    private Bird bird;
    private Texture gameOverText;
    private Texture highScoreText;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;

    private Texture musicOffBtn;
    private Texture musicOnBtn;
    private Rectangle musicBtnBounds;

    private int score;
    private int highScore;
    private String scoreString;
    private BitmapFont scoreFont;
    private BitmapFont highscoreFont;
    private GlyphLayout layout;

    private boolean musicOn;
    private Preferences prefs;

    private Sound deathsound;

    private Array<Tube> tubes;


    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 240);
        gameOverText = new Texture("gameover.png");
        highScoreText = new Texture("highscoretext.png");

        musicOnBtn = new Texture("musiconbtn.png");
        musicOffBtn = new Texture("musicoffbtn.png");

        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

        score = 0;
        scoreString = "0";
        scoreFont = new BitmapFont();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator((Gdx.files.internal("pressstart2p.ttf")));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 22;
        param.borderColor = Color.BLACK;
        param.borderWidth = 2;


        scoreFont = generator.generateFont(param);
        scoreFont.setUseIntegerPositions(false);

        param.size = 12;
        param.borderWidth = 1;

        highscoreFont = generator.generateFont(param);
        highscoreFont.setUseIntegerPositions(false);

        layout = new GlyphLayout();
        layout.setText(highscoreFont, highScore + "");
        generator.dispose();

        prefs = Gdx.app.getPreferences("fbrprefs");
        musicOn = prefs.getBoolean("music");

        if (prefs.getInteger("highScore") >= 0) {
            highScore = prefs.getInteger("highScore");
        } else {
            highScore = 0;
        }

        deathsound = Gdx.audio.newSound(Gdx.files.internal("deathsound.mp3"));

        cam.setToOrtho(false, (float)FlappyDemo.WIDTH / 2, (float)FlappyDemo.HEIGHT / 2);

        tubes = new Array<Tube>();

        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            bird.jump();
        }
        updateGround();
    }

    @Override
    public void update(float dt) {
        handleInput();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + 80;

        for(int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);

            // move tube
            if(cam.position.x - (cam.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                increaseScore();
            }


            if(tube.collides(bird.getBounds())) {
                gameOver();
            }
        }

        if(bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
            gameOver();
        }

        if(bird.getPosition().y > cam.viewportHeight) {
            gameOver();
        }
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);

        scoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        scoreFont.draw(sb, scoreString, cam.position.x - 80, cam.position.y / 7);

        sb.draw(highScoreText, (cam.position.x + 75) - highScoreText.getWidth() / 2, (cam.position.y / 7));

        highscoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        highscoreFont.draw(sb, highScore + "", (cam.position.x + 75) - layout.width / 2, (cam.position.y / 7) - 12);

        if (gsm.getGamePaused()) {
            sb.draw(gameOverText, cam.position.x - gameOverText.getWidth() / 2, cam.position.y);
            if (musicOn) {
                sb.draw(musicOffBtn, cam.position.x - musicOnBtn.getWidth() / 2, cam.position.y - 160);
            } else {
                sb.draw(musicOnBtn, cam.position.x - musicOnBtn.getWidth() / 2, cam.position.y - 160);
            }

            if(Gdx.input.justTouched()) {
                musicBtnBounds = new Rectangle(cam.position.x - musicOnBtn.getWidth() / 2, cam.position.y - 160, musicOnBtn.getWidth(), musicOnBtn.getHeight());
                Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                cam.unproject(tmp);
                // texture x is the x position of the texture
                // texture y is the y position of the texture

                if (musicBtnBounds.contains(tmp.x, tmp.y)) {
                    if (prefs.getBoolean("music")) {
                        musicOn = false;
                        prefs.putBoolean("music", false);
                        prefs.flush();
                    } else {
                        musicOn = true;
                        prefs.putBoolean("music", true);
                        prefs.flush();
                    }
                } else {
                    //unpause game on touch
                    gsm.setPause(false);
                    gsm.set(new PlayState(gsm));
                }
            }
        }

        sb.end();
    }

    @Override
    public void dispose() {
        highScoreText.dispose();
        gameOverText.dispose();
        deathsound.dispose();
        scoreFont.dispose();
        bg.dispose();
        bird.dispose();
        ground.dispose();
        musicOnBtn.dispose();
        musicOffBtn.dispose();
        for(Tube tube : tubes) {
            tube.dispose();
        }
    }

    private void updateGround() {
        if(cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if(cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }

    private void increaseScore() {
        score++;
        scoreString = score + "";

        if (score > highScore) {
            prefs.putInteger("highScore", score);
            prefs.flush();
        }
    }

    private void gameOver() {
        deathsound.play(0.2f);
        gsm.setPause(true);
    }
}
