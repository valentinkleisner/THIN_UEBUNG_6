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
    }

    private void readInput() {
        Scanner scanner = new Scanner(System.in);
        input=scanner.nextLine();
        if (input.matches("([0-9][0-9][+|-]([0-9][0-9][+|-][*|/]?)?)*")) {
            strings = input.split("([0-9][0-9][+|-][*|/]?)");
            for (int i = 0; i < strings.length; i++) {
                char c = strings[i].charAt(i);
                stack.push(c);
            }
        } else {
            System.out.println("Incorrect Input.");
        }
    }

    private void assignOperator(){
    char number1,number2;
        if(input.equals("+") || input.equals("-") || input.equals("*") || input.equals("/"))
        {
            switch (input)
            {
                case '+':
                    number1 = stack.pop();
                    number2 = stack.pop();
                    int summand = (Integer.parseInt(String.valueOf(number1))+ Integer.parseInt(String.valueOf(number2)));
                    stack.push(Integer.toString(summand).charAt(0));
                    System.out.print(number1 + " + " + number2);
                    System.out.print("Result :"+ (summand));
                    break;
                case '-':
                    number1 = stack.pop();
                    number2 = stack.pop();
                    int subtraktion = (Integer.parseInt(String.valueOf(number1))- Integer.parseInt(String.valueOf(number2)));
                    stack.push(Integer.toString(subtraktion).charAt(0));
                    System.out.print(number1 +" - "+ number2);
                    System.out.print("Result :"+ (number1 - number2));
                    break;
                case '/':
                    number1 = stack.pop();
                    number2 = stack.pop();
                    int division = (Integer.parseInt(String.valueOf(number1))/ Integer.parseInt(String.valueOf(number2)));
                    stack.push(Integer.toString(division).charAt(0));
                    System.out.print(number1 +" / "+ number2);
                    System.out.print("Result :"+ (number1 / number2));
                    break;
                case '*':
                    number1 = stack.pop();
                    number2 = stack.pop();
                    int multiplikation = (Integer.parseInt(String.valueOf(number1))* Integer.parseInt(String.valueOf(number2)));
                    stack.push(Integer.toString(multiplikation).charAt(0));
                    System.out.print(number1 +" * "+ number2);
                    System.out.print("Result :"+ (number1 * number2));
                    break;
            }
        }
        else
        {
            stack.push(input);
        }

    }

        private boolean isNumeric(String number)
        {
            try
            {
                int dezimal = Integer.parseInt(number);
            }
            catch(NumberFormatException e)
            {
                return false;
            }
            return true;
        }

    }


