package com.heiko.matisser;

import android.app.Activity;
import android.content.Intent;

public abstract class Transactor {
    private Transactor next;

    public void setNext(Transactor next) {
        this.next = next;
    }

    public Transactor getNext() {
        return next;
    }

    public abstract void handle(Matter matter, Activity activity);

    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return false;
    }
}