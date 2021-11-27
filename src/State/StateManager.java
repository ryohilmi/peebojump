package State;

public class StateManager {

    private State[] gameStates;
    private int currentState;

    public enum StateType {
        MENUSTATE,
    }

    public StateManager() {
        gameStates = new State[StateType.values().length];

        currentState = -1;
        loadState(currentState);
    }

    private void loadState(int state) {
        if (state == StateType.MENUSTATE.ordinal()) {
            //TODO : add menu state
        }

    }

    private void unloadState(int state) {
        gameStates[state] = null;
    }

    public void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    public void update() {
        gameStates[currentState].update();
    }

    public void draw(java.awt.Graphics2D g) {
        gameStates[currentState].draw(g);

    }

    public void keyPressed(int k) {
        gameStates[currentState].keyPressed(k);
    }

    public void keyReleased(int k) {
        gameStates[currentState].keyReleased(k);
    }

}









