package com.yatochk.translator.model

import android.os.AsyncTask

class OnlineTranslateController : TranslateTaskContract {

    lateinit var model: ModelContractControllers

    override fun onGetLanguageList(getLanguagesResult: String) {

    }

    override fun onTranslated(translateResult: String) {
        model.onTranslateComplete(translateResult)
    }

    override fun onLangDefinition(definitionResult: String) {

    }

    fun translate(text: String) {
        val translateTask = TranslateTask(text, this)
        translateTask.execute()
    }

    fun languageDefinition(text: String) {
        val definitionTask = DefinitionTask(text, this)
        definitionTask.execute()
    }

    fun getLanguageList() {
        val languageListTask = LanguageListTask(this)
        languageListTask.execute()
    }

    class TranslateTask(val text: String, private val translateTaskContract: TranslateTaskContract)
        : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null)
                translateTaskContract.onTranslated(result)
        }
    }

    class DefinitionTask(val text: String, private val translateTaskContract: TranslateTaskContract)
        : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null)
                translateTaskContract.onLangDefinition(result)
        }
    }

    class LanguageListTask(private val translateTaskContract: TranslateTaskContract)
        : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {

            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null)
                translateTaskContract.onGetLanguageList(result)
        }
    }
}