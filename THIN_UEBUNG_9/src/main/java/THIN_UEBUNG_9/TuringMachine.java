package THIN_UEBUNG_9;

import java.util.*;

import static THIN_UEBUNG_9.Transition.*;

public class TuringMachine {
    public static final char BLANK = '_';
    public static final char ONE = '1';
    public static final char ZERO = '0';
    private static final int INITIAL_STATE = 1;
    private static final int FINAL_STATE = 2;
    private static final int STEP_MODUS = 0;
    private static final int LAUF_MODUS = 1;

    private static final int TRANSITION_FIRST_STATE = 0;
    private static final int TRANSITION_READ_SYMBOL = 1;
    private static final int TRANSITION_SECOND_STATE = 2;
    private static final int TRANSITION_WRITE_SYMBOL = 3;
    private static final int TRANSITION_MOVE_DIRECTION = 4;

    private List<Character> tape = new LinkedList<>();
    private final Map<Integer, ArrayList<Transition>> states = new HashMap<>();

    private int currentState = INITIAL_STATE;
    private int mode;
    private int tapeHeadPosition;
    private int steps = 0;
    private boolean halt = false;
    private int firstResultIndex;

    public static void main(String[] args) {
        while (true) {
            TuringMachine tm = new TuringMachine();
            tm.setTransitionsAndTape(getAndCheckInput());
            tm.setMode();
            if (tm.start()) {
                System.out.println("Result:     " + tm.getResult() + "\n");
            } else System.out.println("Couldn't get Result!\n");
        }
    }

    private boolean start() {
        printInfoAndTape();

        while (!halt) {
            if (currentState == FINAL_STATE) {
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

    private void setTransitionsAndTape(String binaryString) {
        int stringIndexPos = 0;
        int zeroCount = 0;
        int transitionState = 0;
        int firstState = 0;
        int secondState = 0;
        char readChar = 0;
        char writeChar = 0;
        int moveDirection = 0;
        boolean processedTransitions = false;

        if (binaryString.charAt(stringIndexPos) == ONE) {
            stringIndexPos++;
        }

        while (!processedTransitions) {
            if (binaryString.charAt(stringIndexPos) == ONE) {
                switch (transitionState) {
                    case TRANSITION_FIRST_STATE:
                        firstState = zeroCount;
                        break;
                    case TRANSITION_READ_SYMBOL:
                        if (zeroCount == 1) {
                            readChar = ZERO;
                        } else if (zeroCount == 2) {
                            readChar = ONE;
                        } else if (zeroCount == 3) {
                            readChar = BLANK;
                        } else System.out.println("Invalid TM Encoding.\n");
                        break;
                    case TRANSITION_SECOND_STATE:
                        secondState = zeroCount;
                        break;
                    case TRANSITION_WRITE_SYMBOL:
                        if (zeroCount == 1) {
                            writeChar = ZERO;
                        } else if (zeroCount == 2) {
                            writeChar = ONE;
                        } else if (zeroCount == 3) {
                            writeChar = BLANK;
                        } else System.out.println("Invalid TM Encoding.\n");
                        break;
                    case TRANSITION_MOVE_DIRECTION:
                        if (zeroCount == 1) {
                            moveDirection = LEFT;
                        } else if (zeroCount == 2) {
                            moveDirection = RIGHT;
                        } else System.out.println("Invalid TM Encoding.\n");
                        break;
                }

                zeroCount = 0;
                transitionState++;
                if (transitionState > 4) transitionState = 0;

                if (binaryString.charAt((stringIndexPos + 1)) == ONE) {
                    setTransition(firstState, readChar, secondState, writeChar, moveDirection);
                    if (binaryString.charAt(stringIndexPos + 2) == ONE) {
                        stringIndexPos += 2;
                        processedTransitions = true;
                    } else {
                        stringIndexPos += 1;
                    }
                }
            } else zeroCount++;

            stringIndexPos++;
        }
        initializeTapeWithInput(binaryString.substring(stringIndexPos));
    }

    private void setTransition(int firstState, char readChar, int secondState, char writeChar, int moveDirection) {
        if (!states.containsKey(firstState)) {
            states.put(firstState, new ArrayList<>());
        }
        states.get(firstState).add(new Transition(firstState, secondState, readChar, writeChar, moveDirection));
    }

    private void initializeTapeWithInput(String input) {
        for (int i = 0; i < 500; i++) {
            tape.add(BLANK);
        }
        tapeHeadPosition = tape.size();
        int zeroCount = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ZERO) zeroCount++;

            if (input.charAt(i) == ONE || (i + 1) >= input.length()) {
                if (zeroCount == 1) {
                    tape.add(ZERO);
                } else if (zeroCount == 2) {
                    tape.add(ONE);
                } else if (zeroCount == 3) {
                    tape.add(BLANK);
                }
                zeroCount = 0;
            }
        }
        firstResultIndex = tape.size() + 1;
        for (int i = 0; i < 500; i++) {
            tape.add(BLANK);
        }
    }



    private static String getAndCheckInput() {
        /*boolean isCorrectInput = false;
        Scanner scanner = new Scanner(System.in);
        String regex = "^[0|1]+$";*/
        String binaryInput = "1010010001000100110100010000000000010001011000100100010010011000100010000100010011000010" +
                "01000001000100110000010010000010010011000001000100000010001001100000010010000001001001100000010001000" +
                "00001001011000000010010000000100101100000001000100000000100010110000000010010000000010010110000000010" +
                "00100001001001100000000010010000000001001011000000000100010000000000100010110000000000100100000000001" +
                "001011000000000010001010010011000000000001001000000000001001011000000000001000100100010011" +
                "000010001000000000100010" +
                //multiplication factors  13*17
                //"111001001001001001001001001001001001001001000100100100100100100100100100100100100100100100100100";
                //multiplication factors 2*4
                "111001001000100100100100";
                //multiplication factors 1*27
                //"111001000100100100100100100100100100100100100100100100100100100100100100100100100100100100";
                //multiplication factors 0*23
                //"111000100100100100100100100100100100100100100100100100100100100100100100100";

        /*while (!isCorrectInput) {
            System.out.println("Enter binary encoded TM:");
            String line = scanner.nextLine().trim();
            if (line.matches(regex)) {
                isCorrectInput = true;
                binaryInput = line;
                System.out.println("Correct Input.");
            } else System.out.println("Wrong Input, please try again.\n");
        }*/
        return binaryInput;
    }

    private int getResult() {
       int resultIndex = firstResultIndex;
       StringBuilder binarySB = new StringBuilder();
       while (tape.get(resultIndex) != BLANK) {
           binarySB.append(tape.get(resultIndex));
           resultIndex++;
       }
       return binarySB.length();
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
                Thread.sleep(500);
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
}
