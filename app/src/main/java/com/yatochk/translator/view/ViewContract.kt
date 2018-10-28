package com.yatochk.translator.view

import android.content.Context
import com.yatochk.translator.model.database.DatabaseTranslate

interface ViewContract {
    val context: Context
    var translatedText: String
    var translateText: String
    var fromLanguage: String
    var toLanguage: String
    fun updateSpinnerAdapter(languages: LinkedHashMap<String, String>)
    fun updateTranslateListAdapter(translates: ArrayList<DatabaseTranslate>)
    fun showTranslatedText(text: String)
    fun showToast(message: String)
    fun clearInputText()
}