package com.simbirsoft.model;

public enum OperationType {
    SELLING(0),
    RESCUE(1),
    NOT_DEFINE(2);

    private final int numOfOperation;

    OperationType(int numOfOperation) {
        this.numOfOperation = numOfOperation;
    }

    public int getNumOfOperation() {
        return numOfOperation;
    }
}
