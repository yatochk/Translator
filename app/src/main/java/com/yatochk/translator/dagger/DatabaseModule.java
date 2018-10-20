package com.yatochk.translator.dagger;

import com.yatochk.translator.model.database.DatabaseHelper;

import dagger.Module;
import dagger.Provides;

@Module
class DatabaseModule {
    @Provides
    DatabaseHelper provideDatabaseController() {
        return new DatabaseHelper();
    }
}
