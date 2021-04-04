package THIN_UEBUNG_6;

public class Stack{
    public final String EOS = "$";
    public final Integer STACK_LENGTH = 99;
    String[] stack = new String[STACK_LENGTH];

    public Stack() {
        stack[0] = EOS;
    }

    public String pop() {
        String[] newStack = new String[STACK_LENGTH];
        String removedChar = stack[0];
        for (int i = 0; i < stack.length; i++) {
            newStack[i] = stack[i+1];
            if (stack[i+1].equals(EOS))
                break;
        }
        stack = newStack;

        return removedChar;
    }


    public void push(String character) {
        String[] newStack = new String[STACK_LENGTH];
        newStack[0] = character;
        for (int i = 0; i < stack.length; i++) {
            newStack[i+1] = stack[i];
            if (stack[i].equals(EOS))
                break;
        }
        stack = newStack;
    }

    public String peek() {
        return stack[0];
    }

    public Integer getSize() {
        return stack.length;
    }

}
