package com.yatochk.translator.model

import com.yatochk.translator.model.database.DatabaseController
import com.yatochk.translator.model.translate.OnlineTranslateController
import com.yatochk.translator.model.translate.SUCCESSFUL_TASK
import com.yatochk.translator.model.translate.Translate

class Model(private val databaseController: DatabaseController,
            private val onlineTranslateController: OnlineTranslateController) : ModelContract.Contract,
        Translate.OnTranslateTaskListener {

    lateinit var onModelTaskListener: ModelContract.OnModelTaskListener

    init {
        onlineTranslateController.onTranslateTaskListener = this
        databaseController.model = this
    }

    override fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>, answerCode: Int) {
        if (answerCode == SUCCESSFUL_TASK)
            onModelTaskListener.onGetLanguageListComplete(languageList)
        else
            onModelTaskListener.onGetLanguageListError(answerCode)
    }

    override fun onTranslateComplete(translatedText: String, answerCode: Int) {
        if (answerCode == SUCCESSFUL_TASK) {
            databaseController.addTranslate()
            onModelTaskListener.onTranslateComplete(translatedText)
        } else
            onModelTaskListener.onTranslateError(answerCode)
    }

    override fun translate(text: String, fromLang: String, toLang: String) {

        onlineTranslateController.translate(text, fromLang, toLang)
    }

    override fun languageList(uiLanguage: String) {
        onlineTranslateController.languageList(uiLanguage)
    }
}