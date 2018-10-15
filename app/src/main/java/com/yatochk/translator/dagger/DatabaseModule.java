package com.yatochk.translator.dagger;

import com.yatochk.translator.model.Database;

import dagger.Module;
import dagger.Provides;

@Module
class DatabaseModule {
    @Provides
    Database provideDatabaseController() {
        return new Database();
    }
}
