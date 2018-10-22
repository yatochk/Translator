package com.yatochk.translator.dagger

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerModelComponent.create()
    }

    companion object {
        lateinit var component: ModelComponent
            private set
    }
}

