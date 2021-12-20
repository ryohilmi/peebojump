package State;

import Utility.SaveData;

public class StateManager {

    private State[] gameStates;
    private int currentState;
    private int previousState;

    public static final int NUMGAMESTATES = 2;
    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;

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
        if(state == LEVEL1STATE) {
            gameStates[state] = new Level1State(this);
            SaveData.writeLatestLevel(SaveData.LEVEL1);
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









