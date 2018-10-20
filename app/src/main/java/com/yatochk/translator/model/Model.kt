package com.yatochk.translator.model

import com.yatochk.translator.model.database.Database
import com.yatochk.translator.model.database.DatabaseHelper
import com.yatochk.translator.model.translate.OnlineTranslateController
import com.yatochk.translator.model.translate.SUCCESSFUL_TASK
import com.yatochk.translator.model.translate.Translate

class Model(private val databaseHelper: DatabaseHelper,
            private val onlineTranslateController: OnlineTranslateController) : ModelContract.Contract {

    lateinit var onModelTaskListener: ModelContract.OnModelTaskListener

    init {
        onlineTranslateController.onTranslateTaskListener = object : Translate.OnTranslateTaskListener {
            override fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>, answerCode: Int) {
                if (answerCode == SUCCESSFUL_TASK)
                    onModelTaskListener.onGetLanguageListComplete(languageList)
                else
                    onModelTaskListener.onGetLanguageListError(answerCode)
            }

            override fun onTranslateComplete(translatedText: String, answerCode: Int) {
                if (answerCode == SUCCESSFUL_TASK) {
                    databaseHelper.addTranslate()
                    onModelTaskListener.onTranslateComplete(translatedText)
                } else
                    onModelTaskListener.onTranslateError(answerCode)
            }
        }

        databaseHelper.onDatabaseTaskListener = object : Database.OnDatabaseTaskListener {
            override fun onTranslateRemoved() {

            }

            override fun onTranslateAdded() {

            }

        }
    }

    override fun translate(text: String, fromLang: String, toLang: String) =
            onlineTranslateController.translate(text, fromLang, toLang)

    override fun languageList(uiLanguage: String) =
            onlineTranslateController.languageList(uiLanguage)
}