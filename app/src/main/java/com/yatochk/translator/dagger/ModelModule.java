package com.yatochk.translator.dagger;

import com.yatochk.translator.model.Database;
import com.yatochk.translator.model.Model;
import com.yatochk.translator.model.OnlineTranslate;

import dagger.Module;
import dagger.Provides;

@Module
class ModelModule {

    @Provides
    Model provideModel(Database database, OnlineTranslate onlineTranslate) {
        return new Model(database, onlineTranslate);
    }

}
