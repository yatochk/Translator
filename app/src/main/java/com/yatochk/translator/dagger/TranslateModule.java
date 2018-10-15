package com.yatochk.translator.dagger;

import com.yatochk.translator.model.OnlineTranslate;

import dagger.Module;
import dagger.Provides;

@Module
class TranslateModule {
    @Provides
    OnlineTranslate provideOnlineTranslate() {
        return new OnlineTranslate();
    }
}
