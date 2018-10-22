package com.yatochk.translator.view

import android.content.Context
import com.yatochk.translator.model.database.DatabaseTranslate

interface ViewContract {
    val context: Context
    var translateText: String
    var fromLanguage: String
    var toLanguage: String
    fun openTranslateView()
    fun hideTranslateView()
    fun updateSpinnerAdapter(languages: LinkedHashMap<String, String>)
    fun updateTranslateListAdapter(translates: ArrayList<DatabaseTranslate>)
    fun showTranslatedText(text: String)
    fun showToast(message: String)
}