package com.simbirsoft.model;

public enum OperationsEnum {
    SUPPLY(0),
    SELLING(1),
    RESCUE(2);

    private final int numOfOperation;

    OperationsEnum(int numOfOperation) {
        this.numOfOperation = numOfOperation;
    }

    public int getNumOfOperation() {
        return numOfOperation;
    }
}
