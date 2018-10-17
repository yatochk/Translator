package com.yatochk.translator.presenter

import com.yatochk.translator.ViewContract
import com.yatochk.translator.model.Model

class Presenter(val model: Model) : PresenterContract {

    lateinit var view: ViewContract

    init {
        model.presenter = this
    }

    override fun onTranslateComplete(translatedText: String) {
        view.showTranslatedText(translatedText)
    }

    fun translate() {
        view.openTranslateView()
        model.translate(view.translateText, view.fromLanguage, view.toLanguage)
    }
}
