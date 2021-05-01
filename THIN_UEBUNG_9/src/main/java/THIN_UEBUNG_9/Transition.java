package THIN_UEBUNG_9;

public class Transition {
    public static final int RIGHT = 1;
    public static final int LEFT = -1;

    private int currentState;
    private int nextState;
    private char readCharacter;
    private char writeCharacter;
    private int moveDirection;

    public Transition(int currentState, int nextState, char readCharacter, char writeCharacter, int moveDirection) {
        this.currentState = currentState;
        this.nextState = nextState;
        this.readCharacter = readCharacter;
        this.writeCharacter = writeCharacter;
        this.moveDirection = moveDirection;
    }

    public int getCurrentState() {
        return currentState;
    }

    public int getNextState() {
        return nextState;
    }

    public char getReadCharacter() {
        return readCharacter;
    }

    public char getWriteCharacter() {
        return writeCharacter;
    }

    public int getMoveDirection() {
        return moveDirection;
    }
}
