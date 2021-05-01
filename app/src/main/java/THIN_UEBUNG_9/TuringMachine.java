package THIN_UEBUNG_9;

public class TuringMachine {
    int position = 0;
    char[] machine = new char[100];

    public void write(char symbol, String move) {
        machine[position] = symbol;
        movePosition(move);
    }
    public void movePosition(String move) {
        if(move.equals("L")){
            position--;
        } else if(move.equals("R")) {
            position++;
        } else {
            //donothing
        }
    }
    public void print() {
        for(char c : machine) {
            System.out.print(c + "|");
        }
    }


}
