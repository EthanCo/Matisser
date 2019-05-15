package com.zhihu.matisse.sample;

import android.app.Application;

import com.heiko.matisser.Matisser;

/**
 * App
 *
 * @author Heiko
 * @date 2019/5/15
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Matisser.addTransactor(new UcopTransactor());
        Matisser.addTransactor(new LubanTransactor());
    }
}
