package com.yatochk.translator.model.translate

import android.os.AsyncTask
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

const val API_KEY = "trnsl.1.1.20181014T054759Z.4c070493f9ca3e79.24ae4c52b176d96cf96bb8aa1465d4f2862efcd9"
const val TRANSLATE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate"
const val LANG_LIST_URL = "https://translate.yandex.net/api/v1.5/tr.json/getLangs"
const val LENGTH_ERROR = 413
const val IMPOSSIBLY_ERROR = 422
const val UNKNOWN_ERROR = 0
const val SUCCESSFUL_TASK = 200

class OnlineTranslateController : Translate.Contract {

    lateinit var onTranslateTaskListener: Translate.OnTranslateTaskListener
    private val languageMap = LinkedHashMap<String, String>()

    override fun translate(text: String, fromLang: String, toLang: String) {
        var fromLangCode = ""
        var toLangCode = ""
        for (pair in languageMap.entries) {
            if (fromLang == pair.value)
                fromLangCode = pair.key

            if (toLang == pair.value)
                toLangCode = pair.key
        }
        val languageDirection = "$fromLangCode-$toLangCode"
        val translateTask = TranslateTask(text, languageDirection)

        translateTask.onTranslateListener = object : ServerTaskListener.OnTranslateListener {
            override fun onTranslated(translateResult: String) {
                val jsonObject = JSONObject(translateResult)

                var translateText = jsonObject.getString("text")
                translateText = translateText.substring(2, translateText.lastIndexOf("]") - 1)
                val answerCode = jsonObject.getInt("code")

                onTranslateTaskListener.onTranslateComplete(translateText, answerCode)
            }
        }

        translateTask.execute()
    }

    override fun languageList(uiLanguage: String) {
        val languageListTask = LanguageListTask(uiLanguage)
        languageListTask.onGetLanguageListListener = object : ServerTaskListener.OnGetLanguageListListener {
            override fun onGetLanguageList(languagesResult: String) {
                languageMap.clear()
                var answerCode = SUCCESSFUL_TASK
                val jsonObject = JSONObject(languagesResult)
                if (jsonObject.has("langs")) {
                    val languagesArrayJson = jsonObject.getJSONObject("langs")
                    val languagesNamesJsonArray = languagesArrayJson.names()

                    for (i in 0 until languagesNamesJsonArray.length()) {
                        val name = languagesNamesJsonArray.getString(i)
                        languageMap[name] = languagesArrayJson.getString(name)
                    }
                } else {
                    answerCode = if (jsonObject.has("code")) jsonObject.getInt("code") else UNKNOWN_ERROR
                }

                onTranslateTaskListener.onGetLanguageListComplete(languageMap, answerCode)
            }
        }

        languageListTask.execute()
    }

    class TranslateTask(val text: String, languageDirection: String)
        : ServerTask(url = "$TRANSLATE_URL?key=$API_KEY&text=$text&lang=$languageDirection&callback=translate") {
        lateinit var onTranslateListener: ServerTaskListener.OnTranslateListener

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            if (result != "") {
                val json = result.substring(result.indexOf("(") + 1, result.lastIndexOf(")"))
                onTranslateListener.onTranslated(json)
            }
        }
    }

    class LanguageListTask(uiLanguage: String)
        : ServerTask(url = "$LANG_LIST_URL?key=$API_KEY&ui=$uiLanguage") {
        lateinit var onGetLanguageListListener: ServerTaskListener.OnGetLanguageListListener

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null)
                onGetLanguageListListener.onGetLanguageList(result)
        }
    }

    open class ServerTask(open val url: String) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            var resultTranslate = ""
            try {
                val url = URL(url)

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
    }
}