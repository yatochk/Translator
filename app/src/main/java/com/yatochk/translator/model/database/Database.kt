package com.yatochk.translator.model.database

import android.content.Context

object Database {
    interface Contract {
        fun addTranslate(context: Context, fromLanguage: String, toLanguage: String, fromText: String, toText: String)
        fun removeTranslate(context: Context, removeRowId: String)
        fun getTranslates(context: Context)
    }

    interface OnDatabaseListener {
        fun onGetTranslates(arrayDatabaseTranslates: ArrayList<DatabaseTranslate>)
        fun onTranslateAdded(translate: DatabaseTranslate)
        fun onTranslateRemoved(rowId: String)
    }
}