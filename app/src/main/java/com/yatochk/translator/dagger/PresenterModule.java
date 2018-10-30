package com.yatochk.translator.dagger;

import com.yatochk.translator.model.Model;
import com.yatochk.translator.presenter.Presenter;

import dagger.Module;
import dagger.Provides;

@Module
class PresenterModule {
    @Provides
    Presenter providePresenter(Model model) {
        return new Presenter(model);
    }
}
