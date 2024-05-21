package com.jomariabejo.motorph.utility;

public class Addition {
    private final int primaryNumber;
    private final int secondaryNumber;


    public Addition(int primaryNumber, int secondaryNumber) {
        this.primaryNumber = primaryNumber;
        this.secondaryNumber = secondaryNumber;
    }

    public int add() {
        return (this.primaryNumber + this.secondaryNumber);
    }
}
