package ru.nsu.evdokimov;

public class SynchronousPrinter {
    private boolean turn;

    public SynchronousPrinter(boolean turn) {
        this.turn = turn;
    }

    public boolean getTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}
