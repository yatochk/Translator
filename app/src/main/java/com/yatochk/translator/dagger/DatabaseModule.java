package com.yatochk.translator.dagger;

import com.yatochk.translator.model.database.DatabaseController;

import dagger.Module;
import dagger.Provides;

@Module
class DatabaseModule {
    @Provides
    DatabaseController provideDatabaseController() {
        return new DatabaseController();
    }
}
