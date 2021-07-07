package com.simbirsoft.model;

public enum OperationType {
    SUPPLY(0),
    SELLING(1),
    RESCUE(2);

    private final int numOfOperation;

    OperationType(int numOfOperation) {
        this.numOfOperation = numOfOperation;
    }

    public int getNumOfOperation() {
        return numOfOperation;
    }
}
