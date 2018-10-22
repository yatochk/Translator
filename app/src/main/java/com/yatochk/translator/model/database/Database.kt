package com.yatochk.translator.model.database

import android.content.Context
import android.database.Cursor

object Database {
    interface Contract {
        fun addTranslate(context: Context, fromLanguage: String, toLanguage: String, fromText: String, toText: String)
        fun removeTranslate(context: Context, removeRowId: String)
        fun getTranslates(context: Context)
    }

    interface OnDatabaseListener {
        fun onGetTranslates(arrayDatabaseTranslates: ArrayList<DatabaseTranslate>)
        fun onTranslateAdded()
        fun onTranslateRemoved()
    }
}

object DbTaskListener {
    interface OnAddTaskListener {
        fun onAdded(newRowId: Long)
    }

    interface OnRemoveListener {
        fun onRemoved()
    }

    interface OnGetTranslatesListener {
        fun onGetTranslates(translatesCursor: Cursor)
    }
}