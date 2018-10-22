package com.yatochk.translator.model.database

import android.provider.BaseColumns

object TranslateEntry : BaseColumns {
    const val TABLE_NAME = "translates"

    const val _ID = BaseColumns._ID
    const val COLUMN_NAME_FROM_LANGUAGE = "from_lang"
    const val COLUMN_NAME_TO_LANGUAGE = "to_lang"
    const val COLUMN_NAME_FROM_TEXT = "from_text"
    const val COLUMN_NAME_TO_TEXT = "to_text"
}