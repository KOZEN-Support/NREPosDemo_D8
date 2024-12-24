package com.xc.apex.nre.posdemo.model;

public class ClearOrderEvent {
    private boolean isClear;

    public ClearOrderEvent(boolean isClear) {
        this.isClear = isClear;
    }

    public boolean isClear() {
        return isClear;
    }
}
