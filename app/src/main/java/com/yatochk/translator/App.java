package com.yatochk.translator;

import android.app.Application;

import com.yatochk.translator.dagger.DaggerModelComponent;
import com.yatochk.translator.dagger.ModelComponent;

public class App extends Application {

    private static ModelComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerModelComponent.create();
    }

    public static ModelComponent getComponent() {
        return component;
    }
}

