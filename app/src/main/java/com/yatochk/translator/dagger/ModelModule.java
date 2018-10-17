package com.yatochk.translator.dagger;

import com.yatochk.translator.model.Model;
import com.yatochk.translator.model.database.DatabaseController;
import com.yatochk.translator.model.translate.OnlineTranslateController;

import dagger.Module;
import dagger.Provides;

@Module
class ModelModule {

    @Provides
    Model provideModel(DatabaseController databaseController, OnlineTranslateController onlineTranslateController) {
        return new Model(databaseController, onlineTranslateController);
    }

}
