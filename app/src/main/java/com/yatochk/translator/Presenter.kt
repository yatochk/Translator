package com.yatochk.translator

import com.yatochk.translator.model.Model

class Presenter(val model: Model) : PresenterContractModel {

    lateinit var view: PresenterContractView

    init {
        model.presenter = this
    }

    override fun onTranslateComplete(translatedText: String) {
        view.showTranslatedText(translatedText)
    }

    fun translate(){
    }

}
