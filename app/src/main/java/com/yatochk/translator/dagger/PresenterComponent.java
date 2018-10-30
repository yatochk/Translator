package com.yatochk.translator.dagger;

import com.yatochk.translator.presenter.Presenter;

import dagger.Component;

@Component(modules = {PresenterModule.class, ModelModule.class, DatabaseModule.class, TranslateModule.class})
public interface PresenterComponent {
    Presenter getPresenter();
}
