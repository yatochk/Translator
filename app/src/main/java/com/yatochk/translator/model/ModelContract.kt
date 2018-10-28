package com.yatochk.translator.model

import com.yatochk.translator.model.database.DatabaseTranslate

object ModelContract {
    interface Contract {
        fun savedTranslate()
        fun translate(text: String, fromLang: String, toLang: String)
        fun languageList(uiLanguage: String)
        fun deleteTranslate(deleteRowId: String)
    }

    interface OnModelTaskListener {
        fun onGetSavedTranslate(arrayDatabaseTranslates: ArrayList<DatabaseTranslate>)
        fun onRemoveTranslate(rowId: String)
        fun onAddTranslate(translate: DatabaseTranslate)
        fun onTranslateComplete(translatedText: String)
        fun onTranslateError(errorCode: Int)
        fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>)
        fun onGetLanguageListError(errorCode: Int)
    }
}