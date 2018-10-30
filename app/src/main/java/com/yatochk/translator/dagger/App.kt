package com.yatochk.translator.dagger

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        component = DaggerPresenterComponent.create()
    }

    companion object {
        lateinit var component: PresenterComponent
            private set
    }
}

