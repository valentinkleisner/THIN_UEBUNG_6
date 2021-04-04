package THIN_UEBUNG_6;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Cellar {
    private Stack stack;
    private String input;
    private String[] strings;
    private List<Character> symbol = new ArrayList<>();


    public Cellar() {
        stack = new Stack();
    }

    public void start() {
        readInput();
        calculatePolishNotation();
    }

    private void readInput() {
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            symbol.add(c);
        }
        scanner.close();
    }

    public void calculatePolishNotation() {
        for(char character : symbol) {
            if (isValid(character)) {
                stack.push(character);
                try {
                    handleStack();
                } catch (Exception e) {
                    break;
                }
            } else {
                System.out.println("Expression not accepted unknown Sign.");
            }
        }
    }

    private void handleStack() throws Exception{
        char number1,number2;
        char character = stack.pop();
        try {
            if (isOperator(character)) {
                switch (character) {
                    case '+':
                        number1 = stack.pop();
                        number2 = stack.pop();
                        int summand = (Integer.parseInt(String.valueOf(number1)) + Integer.parseInt(String.valueOf(number2)));
                        stack.push(Integer.toString(summand).charAt(0));
                        System.out.println(number1 + " + " + number2);
                        System.out.print("Result :" + (summand));
                        break;
                    case '-':
                        number1 = stack.pop();
                        number2 = stack.pop();
                        int subtraktion = (Integer.parseInt(String.valueOf(number1)) - Integer.parseInt(String.valueOf(number2)));
                        stack.push(Integer.toString(subtraktion).charAt(0));
                        System.out.print(number1 + " - " + number2);
                        System.out.print("Result :" + (number1 - number2));
                        break;
                    case '/':
                        number1 = stack.pop();
                        number2 = stack.pop();
                        if (!(isZero(number1) | isZero(number2))) {
                            int division = (Integer.parseInt(String.valueOf(number1)) / Integer.parseInt(String.valueOf(number2)));
                            stack.push(Integer.toString(division).charAt(0));
                            System.out.print(number1 + " / " + number2);
                            System.out.print("Result :" + (number1 / number2));
                        }
                        break;
                    case '*':
                        number1 = stack.pop();
                        number2 = stack.pop();
                        int multiplikation = (Integer.parseInt(String.valueOf(number1)) * Integer.parseInt(String.valueOf(number2)));
                        stack.push(Integer.toString(multiplikation).charAt(0));
                        System.out.print(number1 + " * " + number2);
                        System.out.print("Result :" + (number1 * number2));
                        break;
                }
            } else {
                stack.push(input.charAt(0));
            }
        }catch(NumberFormatException e) {
            System.out.println("Expression is not valid " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Expression is not valid " + e.getMessage());
        }
    }

    private boolean isZero(char number1) {
       int digit = Integer.parseInt(String.valueOf(number1));
       if (digit == 0) {
           return true;
       }return false;
    }

    private boolean isNumeric(char character) {
        if(String.valueOf(character).matches("[0-9]")) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isOperator(char character) {
        if(String.valueOf(character).matches("[+|-|*|/]")) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isValid(char character) {
        if(String.valueOf(character).matches("[[0-9]|+|-|*|/]")) {
            return true;
        } else {
            return false;
        }

    }

}


