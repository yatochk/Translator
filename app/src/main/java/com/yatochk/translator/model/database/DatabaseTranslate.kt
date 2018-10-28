package com.yatochk.translator.model.database

data class DatabaseTranslate(val rowId: String, val fromLanguage: String, val toLanguage: String,
                             val from_text: String, val to_text: String)