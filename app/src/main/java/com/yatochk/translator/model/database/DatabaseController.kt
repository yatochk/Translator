package com.yatochk.translator.model.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask

private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${TranslateEntry.TABLE_NAME} (" +
                "${TranslateEntry._ID} INTEGER PRIMARY KEY," +
                "${TranslateEntry.COLUMN_NAME_FROM_LANGUAGE} TEXT," +
                "${TranslateEntry.COLUMN_NAME_TO_LANGUAGE} TEXT," +
                "${TranslateEntry.COLUMN_NAME_FROM_TEXT} TEXT," +
                "${TranslateEntry.COLUMN_NAME_TO_TEXT} TEXT)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TranslateEntry.TABLE_NAME}"

class DatabaseController : Database.Contract {
    lateinit var onDatabaseListener: Database.OnDatabaseListener
    private var databaseHelper: SQLiteOpenHelper? = null
    private var writableDatabase: SQLiteDatabase? = null
    private var readableDatabase: SQLiteDatabase? = null

    override fun addTranslate(context: Context, fromLanguage: String, toLanguage: String, fromText: String, toText: String) {
        databaseHelper = TranslateDbHelper(context)
        writableDatabase = databaseHelper!!.writableDatabase

        val values = ContentValues().apply {
            put(TranslateEntry.COLUMN_NAME_FROM_LANGUAGE, fromLanguage)
            put(TranslateEntry.COLUMN_NAME_TO_LANGUAGE, toLanguage)
            put(TranslateEntry.COLUMN_NAME_FROM_TEXT, fromText)
            put(TranslateEntry.COLUMN_NAME_TO_TEXT, toText)
        }

        val addTranslateTask = AddTranslateTask(writableDatabase!!, values)
        addTranslateTask.setOnAddTranslateListener { rowId ->
            onDatabaseListener.onTranslateAdded(
                    DatabaseTranslate(rowId, fromLanguage, toLanguage, fromText, toText)
            )
        }

        addTranslateTask.execute()
    }

    override fun removeTranslate(context: Context, removeRowId: String) {
        databaseHelper = TranslateDbHelper(context)
        writableDatabase = databaseHelper!!.writableDatabase

        val removeTask = RemoveTranslateTask(writableDatabase!!, removeRowId)
        removeTask.setOnRemoveTranslateListener {
            onDatabaseListener.onTranslateRemoved(removeRowId)
        }
        removeTask.execute()
    }

    override fun getTranslates(context: Context) {
        if (databaseHelper == null) {
            databaseHelper = TranslateDbHelper(context)
            if (readableDatabase == null)
                readableDatabase = databaseHelper!!.readableDatabase
        }
        val getTranslatesTask = GetTranslatesTask(readableDatabase!!)
        getTranslatesTask.setOnGetTranslatesListener { translatesCursor ->
            val translatesArray = ArrayList<DatabaseTranslate>()

            with(translatesCursor) {
                while (moveToNext()) {
                    val rowId = getString(getColumnIndexOrThrow(TranslateEntry._ID))
                    val fromLanguage = getString(getColumnIndexOrThrow(TranslateEntry.COLUMN_NAME_FROM_LANGUAGE))
                    val toLanguage = getString(getColumnIndexOrThrow(TranslateEntry.COLUMN_NAME_TO_LANGUAGE))
                    val fromText = getString(getColumnIndexOrThrow(TranslateEntry.COLUMN_NAME_FROM_TEXT))
                    val toText = getString(getColumnIndexOrThrow(TranslateEntry.COLUMN_NAME_TO_TEXT))

                    translatesArray.add(DatabaseTranslate(
                            rowId,
                            fromLanguage,
                            toLanguage,
                            fromText,
                            toText
                    ))
                }
            }
            translatesCursor.close()

            onDatabaseListener.onGetTranslates(translatesArray)

        }

        getTranslatesTask.execute()
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
        : AsyncTask<Unit, Unit, Long>() {

        private var onAddTranslateListener: ((newRowId: String) -> Unit)? = null
        fun setOnAddTranslateListener(listener: (newRowId: String) -> Unit) {
            onAddTranslateListener = listener
        }

        override fun doInBackground(vararg params: Unit?): Long =
                writableDatabase.insert(TranslateEntry.TABLE_NAME,
                        null, values)

        override fun onPostExecute(newRowId: Long?) {
            super.onPostExecute(newRowId)

            if (newRowId != null)
                onAddTranslateListener?.invoke(newRowId.toString())
        }
    }

    class RemoveTranslateTask(private val writableDatabase: SQLiteDatabase, private val rowId: String)
        : AsyncTask<Unit, Unit, Int>() {
        private var onRemoveTranslateListener: ((deletedRowId: String) -> Unit)? = null
        fun setOnRemoveTranslateListener(listener: (deletedRowId: String) -> Unit) {
            onRemoveTranslateListener = listener
        }

        override fun doInBackground(vararg params: Unit?): Int {
            val selection = "${TranslateEntry._ID} LIKE ?"
            val selectionArgs = arrayOf(rowId)

            return writableDatabase.delete(TranslateEntry.TABLE_NAME, selection, selectionArgs)
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)

            if (result != null)
                onRemoveTranslateListener?.invoke(result.toString())
        }
    }

    class GetTranslatesTask(private val readableDatabase: SQLiteDatabase) : AsyncTask<Void, Void, Cursor>() {
        private var onGetTranslatesListener: ((translatesCursor: Cursor) -> Unit)? = null
        fun setOnGetTranslatesListener(listener: (translatesCursor: Cursor) -> Unit) {
            onGetTranslatesListener = listener
        }

        override fun doInBackground(vararg params: Void?): Cursor {

            return readableDatabase.query(
                    TranslateEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            )
        }

        override fun onPostExecute(translatesCursor: Cursor?) {
            super.onPostExecute(translatesCursor)

            if (translatesCursor != null)
                onGetTranslatesListener?.invoke(translatesCursor)
        }
    }
}