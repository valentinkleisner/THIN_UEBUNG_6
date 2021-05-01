package THIN_UEBUNG_9;

public class Transition {
    private String state;
    private String symbol;
    private String newState;
    private String newSymbol;
    private String move;

    public Transition (String state, String symbol, String newState, String newSymbol, String move ) {
        this.state = state;
        this.symbol = symbol;
        this.newState = state;
        this.newSymbol = symbol;
        this.move = move;
    }



}
