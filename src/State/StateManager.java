package State;

import Utility.SaveData;

public class StateManager {

    private State[] gameStates;
    private int currentState;
    private int previousState;

    public static final int NUMGAMESTATES = 7;
    public static final int MENUSTATE = 0;
    public static final int DEATHSTATE = 1;
    public static final int LEVEL1STATE = 2;
    public static final int LEVEL2STATE = 3;
    public static final int LEVEL3STATE = 4;
    public static final int HIGHSCORESTATE = 5;
    public static final int ABOUTSTATE = 6;


    public StateManager() {

        gameStates = new State[NUMGAMESTATES];

        previousState = -1;
        currentState = MENUSTATE;
        loadState(currentState);
    }

    private void loadState(int state) {
        if(state == MENUSTATE) {
            gameStates[state] = new MenuState(this);
        }
        if(state == DEATHSTATE) {
            gameStates[state] = new DeathState(this);
        }
        if(state == LEVEL1STATE) {
            gameStates[state] = new Level1State(this);
            SaveData.writeLatestLevel(SaveData.LEVEL1);
        }
        if(state == LEVEL2STATE) {
            gameStates[state] = new Level2State(this);
            SaveData.writeLatestLevel(SaveData.LEVEL2);
        }
        if(state == LEVEL3STATE) {
            gameStates[state] = new Level3State(this);
            SaveData.writeLatestLevel(SaveData.LEVEL3);
        }
        if(state == HIGHSCORESTATE) {
            gameStates[state] = new HighScoreState(this);
        }
        if(state == ABOUTSTATE) {
            gameStates[state] = new AboutState(this);
        }
    }

    private void unloadState(int state) {
        gameStates[state] = null;
    }

    public void setState(int state) {
        previousState = currentState;
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    public void update() {
        try {
            if(gameStates[currentState] != null)
            gameStates[currentState].update();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(java.awt.Graphics2D g) {
        try {
            if(gameStates[currentState] != null)
            gameStates[currentState].draw(g);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void keyPressed(int k) {
        gameStates[currentState].keyPressed(k);
    }

    public void keyReleased(int k) {
        gameStates[currentState].keyReleased(k);
    }

}









