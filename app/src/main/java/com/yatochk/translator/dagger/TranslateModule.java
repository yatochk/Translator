package com.yatochk.translator.dagger;

import com.yatochk.translator.model.translate.OnlineTranslateController;

import dagger.Module;
import dagger.Provides;

@Module
class TranslateModule {
    @Provides
    OnlineTranslateController provideOnlineTranslate() {
        return new OnlineTranslateController();
    }
}
