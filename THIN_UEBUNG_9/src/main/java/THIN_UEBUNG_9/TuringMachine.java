package THIN_UEBUNG_9;

import java.util.*;

public class TuringMachine {
    private static final char BLANK = '_';
    private static final char SEPARATOR = '#';
    private static final int INITIAL_STATE = 0;
    private static final int STEP_MODUS = 0;
    private static final int LAUF_MODUS = 1;

    private List<Character> tape = new LinkedList<>();
    private List<String> states = new ArrayList<>();
    private Map<Integer, ArrayList<Transition>> transitions = new HashMap<>();

    private int currentState = INITIAL_STATE;
    private int finalState;
    private int mode;
    private int tapeHeadPosition = 24;
    private int steps = 0;
    private boolean halt = false;

    public TuringMachine()  {
        setTransitions();
    }

    public static void main(String[] args) {
        TuringMachine tm = new TuringMachine();
        tm.setMode();
        String[] binaryFactors = convertDecimalToBinaryString(getAndCheckInput());
        tm.initializeTape(binaryFactors);
        if (tm.start()) {
            System.out.println("Result:     " + tm.getResult());
        } else System.out.println("Error!");
    }

    private void initializeTape(String[] factors) {
        for (int i = 0; i < 24; i++) {
            tape.add(BLANK);
        }

        //add multiplicator plus one separator
        for (int i = 0; i < factors[0].length(); i++) {
            tape.add(factors[0].charAt(i));
        }
        tape.add(SEPARATOR);

        // add multiplicand plus one separator
        for (int i = 0; i < factors[1].length(); i++) {
            tape.add(factors[1].charAt(i));
        }
        tape.add(SEPARATOR);

        for (int i = 0; i < 24; i++) {
            tape.add(BLANK);
        }
    }

    private boolean start() {
        if (mode == STEP_MODUS) printInfo(); printTape();
        while (!halt) {
            List<Transition> possibleTransitions = transitions.get(currentState);
            boolean transitionFound = false;
            for (Transition t : possibleTransitions) {
                if (t.getReadCharacter() == tape.get(tapeHeadPosition)) {
                    tape.set(tapeHeadPosition, t.getWriteCharacter());
                    tapeHeadPosition += t.getMoveDirection();
                    currentState = t.getNextState();
                    transitionFound = true;
                    steps++;
                    break;
                }
            }
            if (currentState == finalState) {
                halt = true;
                return true;
            }
            if (!transitionFound) {
                System.out.println("No valid Transition found.");
                halt = true;
                return false;
            }
            // print Info if Step-Modus + sleep
            if (mode == STEP_MODUS) {
                printInfo();
                printTape();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * Gets the input from the user and checks if its valid. Expected Format (x and y being natural numbers): x*y
     * Returns a String Array holding the two factors for the multiplication.
     * @return factors in String Array
     */
    private static String[] getAndCheckInput() {
        boolean isCorrectInput = false;
        Scanner scanner = new Scanner(System.in);
        String regex = "^[0-9]+(\\*)[0-9]+$";
        String[] factors = new String[2];
        while (!isCorrectInput) {
            System.out.println("Input:");
            String line = scanner.nextLine().trim();
            if (line.matches(regex)) {
                isCorrectInput = true;
                factors = line.split("\\*");
            } else System.out.println("Wrong Input, please try again. Expected Format: x*y");
        }
        return factors;
    }

    private static String[] convertDecimalToBinaryString(String[] decimalFactors) {
        String[] binaryFactors = new String[2];
        binaryFactors[0] = Integer.toBinaryString(Integer.parseInt(decimalFactors[0]));
        binaryFactors[1] = Integer.toBinaryString(Integer.parseInt(decimalFactors[1]));
        return binaryFactors;
    }

    private int getResult() {
        int resultPos = tape.lastIndexOf('#') + 1;
        StringBuilder binarySB = new StringBuilder();
        while (tape.get(resultPos) != BLANK) {
            binarySB.append(tape.get(resultPos));
            resultPos++;
        }
        return Integer.parseInt(binarySB.toString(), 2);
    }

    private void setMode() {
        Scanner scanner = new Scanner(System.in);
        boolean isCorrectInput = false;
        int chosenMode;

        while (!isCorrectInput) {
            System.out.println("Mode: (Step-Modus: 0, Laufmodus: 1)");
            String input = scanner.nextLine().trim();
            if (input.matches("^[01]$")) {
                chosenMode = Integer.parseInt(input);
                this.mode = chosenMode;
                isCorrectInput = true;
            } else {
                System.out.println("Wrong Entry, please try again.");
            }
        }
    }

    private void printTape() {
        int tapeStart = Math.max(tapeHeadPosition - 15, 0);
        int tapeEnd = tapeHeadPosition + 15;
        for (int i = tapeStart; i <= tapeEnd; i++) {
            if (i == tapeHeadPosition) {
                System.out.print("(" + tape.get(i) + ") ");
            } else {
                System.out.print(tape.get(i) + " ");
            }
        }
        System.out.println();
    }

    private void printInfo() {
        System.out.println("\nCurrent State: q" + currentState);
        System.out.println("Number of Steps until now: " + steps);
    }

    private void setTransitions() {
        //TODO set transition & final state
    }

}
