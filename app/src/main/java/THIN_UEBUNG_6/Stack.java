package main.java.THIN_UEBUNG_6;

public class Stack{
    public final char EOS = '$';
    public final Integer STACK_LENGTH = 99;
    char[] stack = new char[STACK_LENGTH];

    public Stack() {
        stack[0] = EOS;
    }

    public void pop() {
        char[] newStack = new char[STACK_LENGTH];
        for (int i = 0; i < stack.length; i++) {
            newStack[i] = stack[i+1];
            if (stack[i+1] == EOS)
                break;
        }
        stack = newStack;
    }

    public void push(char character) {
        char[] newStack = new char[STACK_LENGTH];
        newStack[0] = character;
        for (int i = 0; i < stack.length; i++) {
            newStack[i+1] = stack[i];
            if (stack[i] == EOS)
                break;
        }
        stack = newStack;
    }

    public char peek() {
        return stack[0];
    }

    public Integer getSize() {
        return stack.length;
    }

}
