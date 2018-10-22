package com.yatochk.translator.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.yatochk.translator.R
import com.yatochk.translator.dagger.App
import com.yatochk.translator.model.Model
import com.yatochk.translator.model.database.DatabaseTranslate
import com.yatochk.translator.presenter.Presenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_words.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ViewContract {

    override val context = this
    override var translateText = ""
    override var fromLanguage = "English"
    override var toLanguage = "Russian"

    @Inject
    lateinit var model: Model

    private lateinit var openAnimator: Animator
    private lateinit var presenter: Presenter
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var recyclerAdapter: TranslateRecyclerViewAdapter

    private val languagesNames = LinkedList<String>()
    private val translates = ArrayList<DatabaseTranslate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.component.injectsMainActivity(this)
        presenter = Presenter(model, this)

        openAnimator = AnimatorInflater.loadAnimator(this, R.animator.open_translated)
        openAnimator.setTarget(input_layout)

        input_layout.go_translate.setOnClickListener {
            translateText = input_layout.translate_text.text.toString()
            presenter.translateClick()
        }

        arrayAdapter = ArrayAdapter(this, R.layout.spinner, languagesNames)
        input_layout.from_lang_spinner.adapter = arrayAdapter
        input_layout.to_lang_spinner.adapter = arrayAdapter

        input_layout.from_lang_spinner.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fromLanguage = languagesNames[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        input_layout.to_lang_spinner.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                toLanguage = languagesNames[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        recyclerAdapter = TranslateRecyclerViewAdapter(translates)
        translates_recycle.layoutManager = LinearLayoutManager(this)
        translates_recycle.adapter = recyclerAdapter

        input_layout.input.setOnFocusChangeListener { _, hasFocus ->
            presenter.focusChangeInputText(hasFocus)
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
        this.translates.clear()
        for (translate in translates)
            this.translates.add(translate)

        recyclerAdapter.notifyDataSetChanged()
    }

    override fun openTranslateView() {
        openAnimator.start()
        openAnimator.setupEndValues()
    }

    override fun hideTranslateView() {

    }

    override fun showTranslatedText(text: String) {
        input_layout.translated_text.text = text
    }

    override fun onBackPressed() {
        if (presenter.backPressed())
            super.onBackPressed()
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
