package com.yatochk.translator.dagger;

import com.yatochk.translator.MainActivity;

import dagger.Component;

@Component(modules = {ModelModule.class, DatabaseModule.class, TranslateModule.class})
public interface ModelComponent {
    void injectsMainActivity(MainActivity mainActivity);
}