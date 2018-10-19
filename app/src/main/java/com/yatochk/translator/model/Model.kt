package com.yatochk.translator.model

import com.yatochk.translator.model.database.DatabaseController
import com.yatochk.translator.model.translate.OnlineTranslateController
import com.yatochk.translator.model.translate.SUCCESSFUL_TASK
import com.yatochk.translator.model.translate.Translate

class Model(private val databaseController: DatabaseController,
            private val onlineTranslateController: OnlineTranslateController) : ModelContract.Contract,
        Translate.OnTranslateTaskListener {

    lateinit var presenter: ModelContract.OnModelTaskListener

    init {
        onlineTranslateController.model = this
        databaseController.model = this
    }

    override fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>, answerCode: Int) {
        if (answerCode == SUCCESSFUL_TASK)
            presenter.onGetLanguageListComplete(languageList)
        else
            presenter.onGetLanguageListError(answerCode)
    }

    override fun onTranslateComplete(translatedText: String, answerCode: Int) {
        if (answerCode == 200) {
            databaseController.addTranslate()
            presenter.onTranslateComplete(translatedText)
        } else
            presenter.onTranslateError(answerCode)
    }

    override fun translate(text: String, fromLang: String, toLang: String) {
        onlineTranslateController.translate(text, fromLang, toLang)
    }

    override fun languageList(text: String, fromLang: String, toLang: String) {

    }

    fun getSavedTranslate() {

    }

}