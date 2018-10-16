package com.yatochk.translator.model

import com.yatochk.translator.PresenterContractModel

class Model(private val databaseController: DatabaseController,
            private val onlineTranslateController: OnlineTranslateController) : ModelContractControllers {

    init {
        onlineTranslateController.model = this
        databaseController.model = this
    }

    override fun onTranslateComplete(translatedText: String) {
        presenter.onTranslateComplete(translatedText)
    }

    lateinit var presenter: PresenterContractModel

    fun translate(text: String, fromLang: String, toLang: String) {
        onlineTranslateController.translate(text)
    }

    fun getSavedTranslate() {

    }

}