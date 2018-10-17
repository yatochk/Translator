package com.yatochk.translator.model.translate

import android.os.AsyncTask
import com.yatochk.translator.model.ModelContract
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

const val API_KEY = "trnsl.1.1.20181014T054759Z.4c070493f9ca3e79.24ae4c52b176d96cf96bb8aa1465d4f2862efcd9"
const val TRANSLATE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate"
const val DEFINITE_URL = "https://translate.yandex.net/api/v1.5/tr.json/detect"
const val LANG_LIST_URL = "https://translate.yandex.net/api/v1.5/tr.json/getLangs"

class OnlineTranslateController : TranslateTaskContract {

    lateinit var model: ModelContract

    override fun onGetLanguageList(getLanguagesResult: String) {
        val languageList = LinkedHashMap<String, String>()
        //TODO("Спарсить json из getLanguagesResult в LinkedHashMap<String, String>")
        model.onGetLanguageListComplete(languageList)
    }

    override fun onTranslated(translateResult: String) {
        val jsonObject = JSONObject(translateResult)
        var translateText = jsonObject.getString("text")
        translateText = translateText.substring(2, translateText.lastIndexOf("]") - 1)
        model.onTranslateComplete(translateText)
    }

    override fun onLangDefinition(definitionResult: String) {

    }

    fun translate(text: String, fromLang: String, toLang: String) {
        val translateTask = TranslateTask(text, fromLang, toLang, this)
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

    class TranslateTask(val text: String, val fromLang: String, val toLang: String, private val translateTaskContract: TranslateTaskContract)
        : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {
            var resultTranslate = ""
            try {
                val stringUrl = "$TRANSLATE_URL?key=$API_KEY&text=$text&lang=en-ru&callback=translate"
                val url = URL(stringUrl)

                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                val inputStream = urlConnection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))

                resultTranslate = reader.readLine()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return resultTranslate
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            if (result != "") {
                val json = result.substring(result.indexOf("(") + 1, result.lastIndexOf(")"))
                translateTaskContract.onTranslated(json)
            } else
                translateTaskContract.onTranslated("{\"text\"=\"нихуя\"}")
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