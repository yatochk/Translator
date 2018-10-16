package com.yatochk.translator

import android.app.Application

import com.yatochk.translator.dagger.DaggerModelComponent
import com.yatochk.translator.dagger.ModelComponent

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
