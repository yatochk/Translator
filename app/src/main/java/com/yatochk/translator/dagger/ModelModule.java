package com.yatochk.translator.dagger;

import com.yatochk.translator.model.DatabaseController;
import com.yatochk.translator.model.Model;
import com.yatochk.translator.model.OnlineTranslateController;

import dagger.Module;
import dagger.Provides;

@Module
class ModelModule {

    @Provides
    Model provideModel(DatabaseController databaseController, OnlineTranslateController onlineTranslateController) {
        return new Model(databaseController, onlineTranslateController);
    }

}
