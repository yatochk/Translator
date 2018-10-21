package com.yatochk.translator.model.database

import android.content.Context

object Database {
    interface Contract {
        fun addTranslate(context: Context, fromLanguage: String, toLanguage: String, text: String)
        fun removeTranslate(context: Context, removeRowId: String)
        fun getTranslates(context: Context)
    }

    interface OnDatabaseListener {
        fun onTranslateAdded()
        fun onTranslateRemoved()
    }
}

object DbTaskListener {
    interface OnAddListener {
        fun onAdded(newRowId: Long)
    }

    interface OnRemoveListener {
        fun onRemoved()
    }
}