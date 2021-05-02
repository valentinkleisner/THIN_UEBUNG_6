package THIN_UEBUNG_9;

import java.util.*;

import static THIN_UEBUNG_9.Transition.*;

public class TuringMachine {
    public static final char BLANK = '_';
    public static final char SEPARATOR = '#';
    public static final char PERMANENT_ZERO = '$';
    public static final char PERMANENT_ONE = '%';
    public static final char PROCESSED_ZERO = 'y';
    public static final char PROCESSED_ONE = 'x';
    public static final char ONE = '1';
    public static final char ZERO = '0';
    private static final int INITIAL_STATE = 0;
    private static final int STEP_MODUS = 0;
    private static final int LAUF_MODUS = 1;

    private List<Character> tape = new LinkedList<>();
    private final Map<Integer, ArrayList<Transition>> states = new HashMap<>();

    private int currentState = INITIAL_STATE;
    private int finalState;
    private int mode;
    private int tapeHeadPosition;
    private int steps = 0;
    private boolean halt = false;
    private int lastResultIndex;

    public TuringMachine()  {
        setStatesAndTransitions();
    }

    public static void main(String[] args) {
        while (true) {
            TuringMachine tm = new TuringMachine();
            tm.setMode();
            String[] binaryFactors = convertDecimalToBinaryString(getAndCheckInput());
            tm.initializeTape(binaryFactors);
            if (tm.start()) {
                System.out.println("Result:     " + tm.getResult() + "\n");
            } else System.out.println("Error!\n");
        }
    }

    private void initializeTape(String[] factors) {
        for (int i = 0; i < 24; i++) {
            tape.add(BLANK);
        }
        lastResultIndex = tape.size() - 1;
        tape.add(SEPARATOR);
        //add multiplicator
        for (int i = 0; i < factors[0].length(); i++) {
            tape.add(factors[0].charAt(i));
        }
        tape.add(SEPARATOR);

        // add multiplicand
        for (int i = 0; i < factors[1].length(); i++) {
            tape.add(factors[1].charAt(i));
        }
        tapeHeadPosition = tape.size() - 1;

        for (int i = 0; i < 24; i++) {
            tape.add(BLANK);
        }
    }

    private boolean start() {
        printInfoAndTape();

        while (!halt) {
            if (currentState == finalState) {
                halt = true;
                return true;
            }
            List<Transition> possibleTransitions = states.get(currentState);
            boolean transitionFound = false;
            //find valid Transition
            for (Transition t : possibleTransitions) {
                if (t.getReadCharacter() == tape.get(tapeHeadPosition)) {
                    // write Character, set new tapeHead position, set new State, increment steps
                    tape.set(tapeHeadPosition, t.getWriteCharacter());
                    tapeHeadPosition += t.getMoveDirection();
                    currentState = t.getNextState();
                    transitionFound = true;
                    steps++;
                    break;
                }
            }

            if (!transitionFound) {
                System.out.println("No valid Transition found.");
                halt = true;
                return false;
            }
            printInfoAndTape();
        }
        return false;
    }

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
        int resultPos = lastResultIndex;
        StringBuilder binarySB = new StringBuilder();
        while (tape.get(resultPos) == ZERO || tape.get(resultPos) == ONE) {
            binarySB.append(tape.get(resultPos));
            resultPos--;
        }
        binarySB.reverse();
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

    private void printInfoAndTape() {
        if (mode == STEP_MODUS) {
            printInfo();
            printTape();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    private void setStatesAndTransitions() {
        // q0
        ArrayList<Transition> transitionsState0 = new ArrayList<>(Arrays.asList
                (new Transition(0, 1, ZERO, BLANK, LEFT),
                        new Transition(0, 5, ONE, BLANK, LEFT),
                        new Transition(0, 26, SEPARATOR, SEPARATOR, LEFT)
                ));
        states.put(0, transitionsState0);
        // q1
        ArrayList<Transition> transitionsState1 = new ArrayList<>(Arrays.asList
                (new Transition(1, 1, ZERO, ZERO, LEFT),
                        new Transition(1, 1, ONE, ONE, LEFT),

                        new Transition(1, 2, SEPARATOR, SEPARATOR, LEFT)
                ));
        states.put(1, transitionsState1);
        // q2
        ArrayList<Transition> transitionsState2 = new ArrayList<>(Arrays.asList
                (new Transition(2, 2, ZERO, ZERO, LEFT),
                        new Transition(2, 2, ONE, ONE, LEFT),

                        new Transition(2, 3, SEPARATOR, SEPARATOR, LEFT)
                ));
        states.put(2, transitionsState2);
        // q3
        ArrayList<Transition> transitionsState3 = new ArrayList<>(Arrays.asList
                (new Transition(3, 3, PERMANENT_ONE, PERMANENT_ONE, LEFT),
                        new Transition(3, 3, PERMANENT_ZERO, PERMANENT_ZERO, LEFT),

                        new Transition(3, 4, BLANK, PERMANENT_ZERO, RIGHT),
                        new Transition(3, 4, ZERO, PERMANENT_ZERO, RIGHT),
                        new Transition(3, 4, ONE, PERMANENT_ONE, RIGHT)
                ));
        states.put(3, transitionsState3);
        // q4
        ArrayList<Transition> transitionsState4 = new ArrayList<>(Arrays.asList
                (new Transition(4, 4, PERMANENT_ONE, PERMANENT_ONE, RIGHT),
                        new Transition(4, 4, PERMANENT_ZERO, PERMANENT_ZERO, RIGHT),
                        new Transition(4, 4, SEPARATOR, SEPARATOR, RIGHT),
                        new Transition(4, 4, ZERO, ZERO, RIGHT),
                        new Transition(4, 4, ONE, ONE, RIGHT),

                        new Transition(4, 0, BLANK, BLANK, LEFT)
                ));
        states.put(4, transitionsState4);
        // q5
        ArrayList<Transition> transitionsState5 = new ArrayList<>(Arrays.asList
                (new Transition(5, 5, ONE, ONE, LEFT),
                        new Transition(5, 5, ZERO, ZERO, LEFT),

                        new Transition(5, 6, SEPARATOR, SEPARATOR, LEFT)
                ));
        states.put(5, transitionsState5);
        // q6
        ArrayList<Transition> transitionsState6 = new ArrayList<>(Arrays.asList
                (new Transition(6, 7, ZERO, PROCESSED_ZERO, LEFT),

                        new Transition(6, 11, ONE, PROCESSED_ONE, LEFT)
                ));
        states.put(6, transitionsState6);
        // q7
        ArrayList<Transition> transitionsState7 = new ArrayList<>(Arrays.asList
                (new Transition(7, 7, ONE, ONE, LEFT),
                        new Transition(7, 7, ZERO, ZERO, LEFT),

                        new Transition(7, 8, SEPARATOR, SEPARATOR, LEFT)
                ));
        states.put(7, transitionsState7);
        // q8
        ArrayList<Transition> transitionsState8 = new ArrayList<>(Arrays.asList
                (new Transition(8, 8, PERMANENT_ONE, PERMANENT_ONE, LEFT),
                        new Transition(8, 8, PERMANENT_ZERO, PERMANENT_ZERO, LEFT),

                        new Transition(8, 9, BLANK, PERMANENT_ZERO, RIGHT),
                        new Transition(8, 9, ZERO, PERMANENT_ZERO, RIGHT),
                        new Transition(8, 9, ONE, PERMANENT_ONE, RIGHT)
                ));
        states.put(8, transitionsState8);
        // q9
        ArrayList<Transition> transitionsState9 = new ArrayList<>(Arrays.asList
                (new Transition(9, 9, ONE, ONE, RIGHT),
                        new Transition(9, 9, ZERO, ZERO, RIGHT),
                        new Transition(9, 9, PERMANENT_ZERO, PERMANENT_ZERO, RIGHT),
                        new Transition(9, 9, PERMANENT_ONE, PERMANENT_ONE, RIGHT),
                        new Transition(9, 9, SEPARATOR, SEPARATOR, RIGHT),

                        new Transition(9, 10, PROCESSED_ONE, PROCESSED_ONE, LEFT),
                        new Transition(9, 10, PROCESSED_ZERO, PROCESSED_ZERO, LEFT)
                ));
        states.put(9, transitionsState9);
        // q10
        ArrayList<Transition> transitionsState10 = new ArrayList<>(Arrays.asList
                (new Transition(10, 17, ZERO, PROCESSED_ZERO, LEFT),
                        new Transition(10, 21, ONE, PROCESSED_ONE, LEFT),
                        new Transition(10, 24, SEPARATOR, SEPARATOR, LEFT)
                ));
        states.put(10, transitionsState10);
        // q11
        ArrayList<Transition> transitionsState11 = new ArrayList<>(Arrays.asList
                (new Transition(11, 11, ONE, ONE, LEFT),
                        new Transition(11, 11, ZERO, ZERO, LEFT),

                        new Transition(11, 12, SEPARATOR, SEPARATOR, LEFT)
                ));
        states.put(11, transitionsState11);
        // q12
        ArrayList<Transition> transitionsState12 = new ArrayList<>(Arrays.asList
                (new Transition(12, 12, PERMANENT_ONE, PERMANENT_ONE, LEFT),
                        new Transition(12, 12, PERMANENT_ZERO, PERMANENT_ZERO, LEFT),

                        new Transition(12, 13, BLANK, PERMANENT_ONE, RIGHT),
                        new Transition(12, 13, ZERO, PERMANENT_ONE, RIGHT),
                        new Transition(12, 14, ONE, PERMANENT_ZERO, LEFT)
                ));
        states.put(12, transitionsState12);
        // q13
        ArrayList<Transition> transitionsState13 = new ArrayList<>(Arrays.asList
                (new Transition(13, 13, ONE, ONE, RIGHT),
                        new Transition(13, 13, ZERO, ZERO, RIGHT),
                        new Transition(13, 13, PERMANENT_ONE, PERMANENT_ONE, RIGHT),
                        new Transition(13, 13, PERMANENT_ZERO, PERMANENT_ZERO, RIGHT),
                        new Transition(13, 13, SEPARATOR, SEPARATOR, RIGHT),

                        new Transition(13, 10, PROCESSED_ONE, PROCESSED_ONE, LEFT),
                        new Transition(13, 10, PROCESSED_ZERO, PROCESSED_ZERO, LEFT)
                ));
        states.put(13, transitionsState13);
        // q14
        ArrayList<Transition> transitionsState14 = new ArrayList<>(Arrays.asList
                (new Transition(14, 14, ONE, ZERO, LEFT),

                        new Transition(14, 13, BLANK, ONE, RIGHT),
                        new Transition(14, 13, ZERO, ONE, RIGHT)
                ));
        states.put(14, transitionsState14);
        // q17
        ArrayList<Transition> transitionsState17 = new ArrayList<>(Arrays.asList
                (new Transition(17, 17, ONE, ONE, LEFT),
                        new Transition(17, 17, ZERO, ZERO, LEFT),

                        new Transition(17, 18, SEPARATOR, SEPARATOR, LEFT)
                ));
        states.put(17, transitionsState17);
        // q18
        ArrayList<Transition> transitionsState18 = new ArrayList<>(Arrays.asList
                (new Transition(18, 18, PERMANENT_ONE, PERMANENT_ONE, LEFT),
                        new Transition(18, 18, PERMANENT_ZERO, PERMANENT_ZERO, LEFT),
                        new Transition(18, 18, PROCESSED_ONE, PROCESSED_ONE, LEFT),
                        new Transition(18, 18, PROCESSED_ZERO, PROCESSED_ZERO, LEFT),

                        new Transition(18, 19, ONE, PROCESSED_ONE, RIGHT),
                        new Transition(18, 19, ZERO, PROCESSED_ZERO, RIGHT),
                        new Transition(18, 19, BLANK, PROCESSED_ZERO, RIGHT)
                ));
        states.put(18, transitionsState18);
        // q19
        ArrayList<Transition> transitionsState19 = new ArrayList<>(Arrays.asList
                (new Transition(19, 19, ONE, ONE, RIGHT),
                        new Transition(19, 19, ZERO, ZERO, RIGHT),
                        new Transition(19, 19, PROCESSED_ONE, PROCESSED_ONE, RIGHT),
                        new Transition(19, 19, PROCESSED_ZERO, PROCESSED_ZERO, RIGHT),
                        new Transition(19, 19, PERMANENT_ONE, PERMANENT_ONE, RIGHT),
                        new Transition(19, 19, PERMANENT_ZERO, PERMANENT_ZERO, RIGHT),

                        new Transition(19, 20, SEPARATOR, SEPARATOR, RIGHT)
                ));
        states.put(19, transitionsState19);
        // q20
        ArrayList<Transition> transitionsState20 = new ArrayList<>(Arrays.asList
                (new Transition(20, 20, ONE, ONE, RIGHT),
                        new Transition(20, 20, ZERO, ZERO, RIGHT),

                        new Transition(20, 10, PROCESSED_ONE, PROCESSED_ONE, LEFT),
                        new Transition(20, 10, PROCESSED_ZERO, PROCESSED_ZERO, LEFT)
                ));
        states.put(20, transitionsState20);
        // q21
        ArrayList<Transition> transitionsState21 = new ArrayList<>(Arrays.asList
                (new Transition(21, 21, ONE, ONE, LEFT),
                        new Transition(21, 21, ZERO, ZERO, LEFT),

                        new Transition(21, 22, SEPARATOR, SEPARATOR, LEFT)
                ));
        states.put(21, transitionsState21);
        // q22
        ArrayList<Transition> transitionsState22 = new ArrayList<>(Arrays.asList
                (new Transition(22, 22, PERMANENT_ONE, PERMANENT_ONE, LEFT),
                        new Transition(22, 22, PERMANENT_ZERO, PERMANENT_ZERO, LEFT),
                        new Transition(22, 22, PROCESSED_ONE, PROCESSED_ONE, LEFT),
                        new Transition(22, 22, PROCESSED_ZERO, PROCESSED_ZERO, LEFT),

                        new Transition(22, 19, ZERO, PROCESSED_ONE, RIGHT),
                        new Transition(22, 19, BLANK, PROCESSED_ONE, RIGHT),
                        new Transition(22, 23, ONE, PROCESSED_ZERO, LEFT)
                ));
        states.put(22, transitionsState22);
        // q23
        ArrayList<Transition> transitionsState23 = new ArrayList<>(Arrays.asList
                (new Transition(23, 23, ONE, ZERO, LEFT),

                        new Transition(23, 19, BLANK, ONE, RIGHT),
                        new Transition(23, 19, ZERO, ONE, RIGHT)
                ));
        states.put(23, transitionsState23);
        // q24
        ArrayList<Transition> transitionsState24 = new ArrayList<>(Arrays.asList
                (new Transition(24, 24, PERMANENT_ONE, PERMANENT_ONE, LEFT),
                        new Transition(24, 24, PERMANENT_ZERO, PERMANENT_ZERO, LEFT),
                        new Transition(24, 24, PROCESSED_ONE, PROCESSED_ONE, LEFT),
                        new Transition(24, 24, PROCESSED_ZERO, PROCESSED_ZERO, LEFT),

                        new Transition(24, 25, ONE, ONE, RIGHT),
                        new Transition(24, 25, ZERO, ZERO, RIGHT),
                        new Transition(24, 25, BLANK, BLANK, RIGHT)
                ));
        states.put(24, transitionsState24);
        // q25
        ArrayList<Transition> transitionsState25 = new ArrayList<>(Arrays.asList
                (new Transition(25, 25, PROCESSED_ONE, ONE, RIGHT),
                        new Transition(25, 25, PROCESSED_ZERO, ZERO, RIGHT),
                        new Transition(25, 25, PERMANENT_ONE, PERMANENT_ONE, RIGHT),
                        new Transition(25, 25, PERMANENT_ZERO, PERMANENT_ZERO, RIGHT),
                        new Transition(25, 25, ONE, ONE, RIGHT),
                        new Transition(25, 25, ZERO, ZERO, RIGHT),
                        new Transition(25, 25, SEPARATOR, SEPARATOR, RIGHT),

                        new Transition(25, 0, BLANK, BLANK, LEFT)
                ));
        states.put(25, transitionsState25);
        // q26
        ArrayList<Transition> transitionsState26 = new ArrayList<>(Arrays.asList
                (new Transition(26, 26, ONE, ONE, LEFT),
                        new Transition(26, 26, ZERO, ZERO, LEFT),
                        new Transition(26, 26, PERMANENT_ONE, ONE, LEFT),
                        new Transition(26, 26, PERMANENT_ZERO, ZERO, LEFT),
                        new Transition(26, 26, PROCESSED_ONE, ONE, LEFT),
                        new Transition(26, 26, PROCESSED_ZERO, ZERO, LEFT),
                        new Transition(26, 26, SEPARATOR, SEPARATOR, LEFT),

                        new Transition(26, 27, BLANK, BLANK, RIGHT)
                ));
        states.put(26, transitionsState26);
        //q27 final
        states.put(27, null);
        finalState = 27;
    }

}
