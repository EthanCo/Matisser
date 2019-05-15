package com.heiko.matisser;

import android.app.Activity;

public abstract class Responsibility {
    private Responsibility next;

    public void setNext(Responsibility next) {
        this.next = next;
    }

    public Responsibility getNext() {
        return next;
    }

    //处理请求的方法
    public abstract void handleRequest(String request, Activity activity);
}