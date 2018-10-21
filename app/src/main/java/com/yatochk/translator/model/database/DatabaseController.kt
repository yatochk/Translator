package com.yatochk.translator.model.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask

private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TranslateDbContract.TranslateEntry.TABLE_NAME} (" +
                "${TranslateDbContract.TranslateEntry._ID} INTEGER PRIMARY KEY," +
                "${TranslateDbContract.TranslateEntry.COLUMN_NAME_FROM_LANGUAGE} TEXT," +
                "${TranslateDbContract.TranslateEntry.COLUMN_NAME_TO_LANGUAGE} TEXT," +
                "${TranslateDbContract.TranslateEntry.COLUMN_NAME_TEXT} TEXT)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TranslateDbContract.TranslateEntry.TABLE_NAME}"

class DatabaseController : Database.Contract {

    lateinit var onDatabaseListener: Database.OnDatabaseListener
    private var databaseHelper: SQLiteOpenHelper? = null
    private var writableDatabase: SQLiteDatabase? = null

    override fun addTranslate(context: Context, fromLanguage: String, toLanguage: String, text: String) {
        if (databaseHelper == null) {
            databaseHelper = TranslateDbHelper(context)
            writableDatabase = databaseHelper!!.writableDatabase
        }

        val values = ContentValues().apply {
            put(TranslateDbContract.TranslateEntry.COLUMN_NAME_FROM_LANGUAGE, fromLanguage)
            put(TranslateDbContract.TranslateEntry.COLUMN_NAME_TO_LANGUAGE, toLanguage)
            put(TranslateDbContract.TranslateEntry.COLUMN_NAME_TEXT, text)
        }

        val addTranslateTask = AddTranslateTask(writableDatabase!!, values)
        addTranslateTask.onAddListener = object : DbTaskListener.OnAddListener {
            override fun onAdded(newRowId: Long) {
                onDatabaseListener.onTranslateAdded()
            }
        }

        addTranslateTask.execute()
    }

    override fun removeTranslate(context: Context, removeRowId: String) {
        if (databaseHelper == null) {
            databaseHelper = TranslateDbHelper(context)
            writableDatabase = databaseHelper!!.writableDatabase
        }

        RemoveTranslateTask(writableDatabase!!, removeRowId).execute()
    }

    override fun getTranslates(context: Context) {
    }

    class TranslateDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }

        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "Translator.db"
        }
    }

    class AddTranslateTask(private val writableDatabase: SQLiteDatabase, private val values: ContentValues)
        : AsyncTask<Void, Void, Long>() {
        lateinit var onAddListener: DbTaskListener.OnAddListener

        override fun doInBackground(vararg params: Void?): Long =
                writableDatabase.insert(TranslateDbContract.TranslateEntry.TABLE_NAME,
                        null, values)

        override fun onPostExecute(newRowId: Long?) {
            super.onPostExecute(newRowId)

            if (newRowId != null)
                onAddListener.onAdded(newRowId)
        }
    }

    class RemoveTranslateTask(private val writableDatabase: SQLiteDatabase, private val rowId: String)
        : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            val selection = "${TranslateDbContract.TranslateEntry._ID} LIKE ?"
            val selectionArgs = arrayOf(rowId)

            return writableDatabase.delete(TranslateDbContract.TranslateEntry.TABLE_NAME, selection, selectionArgs)
        }
    }
}