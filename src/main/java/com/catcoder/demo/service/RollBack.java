package com.catcoder.demo.service;

public class RollBack {

    private Boolean isRollBack;

    public Boolean getRollBack() {
        return isRollBack;
    }

    public void setRollBack(Boolean rollBack) {
        isRollBack = rollBack;
    }

    public RollBack(Boolean isRollBack) {
        this.isRollBack = isRollBack;
    }

}
