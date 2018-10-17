package com.yatochk.translator.model

import com.yatochk.translator.model.database.DatabaseController
import com.yatochk.translator.model.translate.OnlineTranslateController
import com.yatochk.translator.presenter.Presenter

class Model(private val databaseController: DatabaseController,
            private val onlineTranslateController: OnlineTranslateController) : ModelContract {

    init {
        onlineTranslateController.model = this
        databaseController.model = this
    }

    override fun onLangDefinitionComplete(language: String) {
    }

    override fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>) {
    }

    override fun onTranslateComplete(translatedText: String) {
        presenter.onTranslateComplete(translatedText)
        databaseController.addTranslate()
    }

    lateinit var presenter: Presenter

    fun translate(text: String, fromLang: String, toLang: String) {
        onlineTranslateController.translate(text, fromLang, toLang)
    }

    fun getSavedTranslate() {

    }

}