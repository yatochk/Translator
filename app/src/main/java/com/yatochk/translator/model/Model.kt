package com.yatochk.translator.model

import android.content.Context
import com.yatochk.translator.model.database.Database
import com.yatochk.translator.model.database.DatabaseController
import com.yatochk.translator.model.translate.OnlineTranslateController
import com.yatochk.translator.model.translate.SUCCESSFUL_TASK
import com.yatochk.translator.model.translate.Translate

class Model(private val databaseController: DatabaseController,
            private val onlineTranslateController: OnlineTranslateController) : ModelContract.Contract {

    lateinit var onModelTaskListener: ModelContract.OnModelTaskListener
    lateinit var context: Context

    init {
        onlineTranslateController.onTranslateTaskListener = object : Translate.OnTranslateTaskListener {
            override fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>, answerCode: Int) {
                if (answerCode == SUCCESSFUL_TASK)
                    onModelTaskListener.onGetLanguageListComplete(languageList)
                else
                    onModelTaskListener.onGetLanguageListError(answerCode)
            }

            override fun onTranslateComplete(answerCode: Int, translatedText: String, fromLang: String, toLang: String) {
                if (answerCode == SUCCESSFUL_TASK) {
                    databaseController.addTranslate(context, fromLang, toLang, translatedText)
                    onModelTaskListener.onTranslateComplete(translatedText)
                } else
                    onModelTaskListener.onTranslateError(answerCode)
            }
        }

        databaseController.onDatabaseListener = object : Database.OnDatabaseListener {
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