package THIN_UEBUNG_6;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Cellar {
    private Stack stack;
    private String input;
    private String[] strings;
    private List<String> symbol;
    private Scanner scanner;


    public Cellar() {
        stack = new Stack();
    }

    public void start() {
        scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            input = scanner.nextLine();
            fillSymbols();
            calculatePolishNotation();
        }

    }


    private void fillSymbols() {
        symbol = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            symbol.add(String.valueOf(c));
        }
    }

    public void calculatePolishNotation() {
        for(String character : symbol) {
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
        if(!stack.peek().equals(stack.EOS)) {
            String result = stack.pop();
            if (isNumeric(result)) {
                System.out.println("Result: " + result);
            } else {
                System.out.println("Expression not accepted. Stack not empty");
            }
        }
    }

    private void handleStack() throws Exception{
        String number1,number2;
        String character = stack.pop();
        try {
            if (isOperator(character)) {
                switch (character) {
                    case "+":
                        number1 = stack.pop();
                        number2 = stack.pop();
                        int summand = (Integer.parseInt(number1) + Integer.parseInt(number2));
                        stack.push(Integer.toString(summand));
                        break;
                    case "-":
                        number1 = stack.pop();
                        number2 = stack.pop();
                        int subtraktion = (Integer.parseInt(String.valueOf(number1)) - Integer.parseInt(String.valueOf(number2)));
                        stack.push(Integer.toString(subtraktion));
                        break;
                    case "/":
                        number1 = stack.pop();
                        number2 = stack.pop();
                        if (!(isZero(number1) | isZero(number2))) {
                            int division = (Integer.parseInt(String.valueOf(number1)) / Integer.parseInt(String.valueOf(number2)));
                            stack.push(Integer.toString(division));
                        }
                        break;
                    case "*":
                        number1 = stack.pop();
                        number2 = stack.pop();
                        int multiplikation = (Integer.parseInt(number1) * Integer.parseInt(String.valueOf(number2)));
                        stack.push(Integer.toString(multiplikation));
                        break;
                }
            } else {
                stack.push(character);
            }
        }catch(NumberFormatException e) {
            System.out.println("Expression is not valid " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Expression is not valid " + e.getMessage());
        }
    }

    private boolean isZero(String number1) {
       int digit = Integer.parseInt(number1);
       if (digit == 0) {
           return true;
       }return false;
    }

    private boolean isNumeric(String character) {
        if(character.matches("[1-9][0-9]*")) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isOperator(String character) {
        if(character.matches("[+|-|*|/]")) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isValid(String character) {
        if(character.matches("[[0-9]|+|-|*|/]")) {
            return true;
        } else {
            return false;
        }

    }

}


