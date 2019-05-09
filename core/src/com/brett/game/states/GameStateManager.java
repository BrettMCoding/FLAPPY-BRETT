package com.brett.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {

    private Stack<State> states;

    private boolean gamePaused = false;

    public GameStateManager(){
        states = new Stack<State>();
    }

    public void push(State state) {
        states.push(state);
    }

    public void pop() {
        states.pop().dispose();
    }

    public void set(State state) {
        states.pop().dispose();;
        states.push(state);
    }

    public void setPause(boolean pause) {
        this.gamePaused = pause;
    }

    public boolean getGamePaused() {
        return gamePaused;
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public State getCurrentState() {
        return states.peek();
    }

    public void render(SpriteBatch sb) {
        states.peek().render(sb);
    }
}
