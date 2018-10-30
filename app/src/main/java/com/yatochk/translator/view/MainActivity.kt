package com.yatochk.translator.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.yatochk.translator.R
import com.yatochk.translator.dagger.App
import com.yatochk.translator.model.database.DatabaseTranslate
import com.yatochk.translator.presenter.Presenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_words.view.*
import kotlinx.android.synthetic.main.show_translate.view.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), ViewContract {
    override val context = this
    override var translatedText = ""
    override var translateText = ""
    override var fromLanguage = "English"
    override var toLanguage = "Russian"

    private lateinit var presenter: Presenter
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var recyclerAdapter: TranslateRecyclerViewAdapter

    private val languagesNames = LinkedList<String>()
    private var translates = ArrayList<DatabaseTranslate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = App.component.presenter
        presenter.attachView(this)

        show_layout.translated_text.movementMethod = ScrollingMovementMethod()
        show_layout.translated_text.setOnLongClickListener {
            presenter.longClickTranslatedText()
            true
        }

        input_layout.go_translate.setOnClickListener {
            translateText = input_layout.translate_text.text.toString()
            presenter.translateClick()
        }

        arrayAdapter = ArrayAdapter(this, R.layout.spinner_row, R.id.language, languagesNames)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_row_drop)
        input_layout.from_lang_spinner.adapter = arrayAdapter
        input_layout.to_lang_spinner.adapter = arrayAdapter

        input_layout.from_lang_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fromLanguage = languagesNames[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        input_layout.to_lang_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                toLanguage = languagesNames[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        input_layout.swap_direction_button.setOnClickListener {
            val fromPosition = input_layout.from_lang_spinner.selectedItemPosition
            input_layout.from_lang_spinner.setSelection(input_layout.to_lang_spinner.selectedItemPosition)
            input_layout.to_lang_spinner.setSelection(fromPosition)
        }

        recyclerAdapter = TranslateRecyclerViewAdapter(translates)
        recyclerAdapter.setOnClickDeleteListener { itemId ->
            presenter.deleteClick(itemId)
        }

        recyclerAdapter.setOnClickTranslateListener {
            show_layout.translated_text.text = it.to_text
            input_layout.translate_text.text.clear()
            input_layout.translate_text.setText(it.from_text)
            selectSpinnerItemByValue(input_layout.from_lang_spinner, it.fromLanguage)
            selectSpinnerItemByValue(input_layout.to_lang_spinner, it.toLanguage)
        }

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.stackFromEnd = true
        with(translates_recycle) {
            itemAnimator = DefaultItemAnimator()
            this.layoutManager = layoutManager
            adapter = recyclerAdapter
        }
    }

    private fun selectSpinnerItemByValue(spinner: Spinner, value: String) {
        val adapter = spinner.adapter
        for (position in 0 until adapter.count) {
            if (adapter.getItem(position) == value) {
                spinner.setSelection(position)
                return
            }
        }
    }

    override fun updateSpinnerAdapter(languages: LinkedHashMap<String, String>) {
        languagesNames.clear()
        for (name in languages.values)
            languagesNames.add(name)

        arrayAdapter.notifyDataSetChanged()
        input_layout.from_lang_spinner.setSelection(languages.values.indexOf(fromLanguage))
        input_layout.to_lang_spinner.setSelection(languages.values.indexOf(toLanguage))
    }

    override fun updateTranslateListAdapter(translates: ArrayList<DatabaseTranslate>) {
        for (translate in translates)
            this.translates.add(translate)

        recyclerAdapter.notifyDataSetChanged()
    }

    override fun addTranslate(translate: DatabaseTranslate) {
        translates.add(translate)
        translates_recycle.smoothScrollToPosition(translates_recycle.adapter.itemCount - 1)
        recyclerAdapter.notifyItemInserted(translates.size - 1)
    }

    override fun removeTranslate(rowId: String) {
        val removePosition = translates.indexOfFirst { it.rowId == rowId }
        recyclerAdapter.notifyItemRemoved(removePosition)
        translates.removeAt(removePosition)
    }

    override fun showTranslatedText(text: String) {
        translatedText = text
        show_layout.translated_text.text = text
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun clearInputText() {
        input_layout.translate_text.text.clear()
    }
}
