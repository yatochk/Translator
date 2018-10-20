package com.yatochk.translator.model.database

interface Database {
    interface Contract {
        fun addTranslate()
        fun removeTranslate()
        fun getTranslates()
    }

    interface OnDatabaseTaskListener {
        fun onTranslateAdded()
        fun onTranslateRemoved()
    }
}