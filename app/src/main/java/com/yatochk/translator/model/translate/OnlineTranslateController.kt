package com.yatochk.translator.model.translate

import android.os.Handler
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

const val API_KEY = "trnsl.1.1.20181014T054759Z.4c070493f9ca3e79.24ae4c52b176d96cf96bb8aa1465d4f2862efcd9"
const val TRANSLATE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate"
const val LANG_LIST_URL = "https://translate.yandex.net/api/v1.5/tr.json/getLangs"
const val LENGTH_ERROR = 413
const val IMPOSSIBLY_ERROR = 422
const val VOID_TASK_ERROR = 0
const val CONNECTION_ERROR = 111
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

        val translateTask = ServerTask("$TRANSLATE_URL?key=$API_KEY&text=$text&lang=$languageDirection")
        translateTask.setOnTaskCompleteListener { translateResult ->
            val jsonObject = JSONObject(translateResult)
            val answerCode = jsonObject.getInt("code")
            var translatedText = ""

            if (answerCode != CONNECTION_ERROR && answerCode != VOID_TASK_ERROR) {
                translatedText = jsonObject.getString("text")
                translatedText = translatedText.substring(2, translatedText.lastIndexOf("]") - 1)
            }

            onTranslateTaskListener.onTranslateComplete(answerCode, text, translatedText, fromLang, toLang)
        }

        translateTask.startTask()
    }

    override fun languageList(uiLanguage: String) {
        val languageListTask = ServerTask("$LANG_LIST_URL?key=$API_KEY&ui=$uiLanguage")
        languageListTask.setOnTaskCompleteListener { languagesResult ->
            languageMap.clear()
            val jsonObject = JSONObject(languagesResult)
            var answerCode = SUCCESSFUL_TASK
            if (jsonObject.has("langs")) {
                val languagesArrayJson = jsonObject.getJSONObject("langs")
                val languagesNamesJsonArray = languagesArrayJson.names()

                for (i in 0 until languagesNamesJsonArray.length()) {
                    val name = languagesNamesJsonArray.getString(i)
                    languageMap[name] = languagesArrayJson.getString(name)
                }
            } else {
                answerCode = if (jsonObject.has("code")) jsonObject.getInt("code") else VOID_TASK_ERROR
            }

            onTranslateTaskListener.onGetLanguageListComplete(languageMap, answerCode)
        }

        languageListTask.startTask()
    }

    open class ServerTask(private val url: String) {
        private val handler = Handler()
        private var onTaskCompleteListener: ((result: String) -> Unit)? = null

        fun setOnTaskCompleteListener(listener: ((result: String) -> Unit)) {
            onTaskCompleteListener = listener
        }

        private val taskThread = Thread {
            var result: String
            try {
                val url = URL(url)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                val inputStream = urlConnection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                result = reader.readLine()

            } catch (e: FileNotFoundException) {
                result = "{ \"code\": $VOID_TASK_ERROR}"
            } catch (e: Exception) {
                result = "{ \"code\": $CONNECTION_ERROR}"
            }

            handler.post {
                onTaskCompleteListener?.invoke(result)
            }
        }

        fun startTask() = taskThread.start()
    }
}