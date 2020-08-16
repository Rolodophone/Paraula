package net.rolodophone.paraula

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import net.rolodophone.paraula.learning.*
import net.rolodophone.paraula.multiplatform.log

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		log = { Log.i("multiplatform", it) }


		// get levels from json
		val moshi = Moshi.Builder().build()
		examples = ExamplesJsonAdapter(moshi).fromJson(readFile(resources, R.raw.examples))!!
		levels = LevelsJsonAdapter(moshi).fromJson(readFile(resources, R.raw.levels))!!.levels

		// for reading the new words json file at runtime
		newWordsJsonAdapter = NewWordsJsonAdapter(moshi)

		val newWords = newWordsJsonAdapter.fromJson(readFile(resources, R.raw.new_words))!!.newWords
		val seenNewWords = newWords.take(getIndexOfNextNewWord(this))
		val wordProbabilities = getWordProbabilities(this)
		seenWords = seenNewWords.mapIndexed { i, word -> SeenWord(word, wordProbabilities[i]) }.toMutableSet()
	}
}