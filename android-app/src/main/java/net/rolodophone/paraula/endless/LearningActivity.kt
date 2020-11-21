package net.rolodophone.paraula.endless

import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.Moshi
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningActivityBinding
import net.rolodophone.paraula.getWordProbabilities
import net.rolodophone.paraula.readFile
import net.rolodophone.paraula.setWordProbabilities
import org.nield.kotlinstatistics.WeightedDice
import kotlin.random.Random.Default.nextInt

class LearningActivity : AppCompatActivity() {

	companion object {
		const val INDEX_OF_NEXT_NEW_WORD = "INDEX_OF_NEXT_NEW_WORD"
		const val ENDLESS_COMPLETED_DIALOG_SEEN = "ENDLESS_COMPLETED_DIALOG_SEEN"
	}

	lateinit var binding: LearningActivityBinding

	private var correctMediaPlayer: MediaPlayer? = null
	private var wrongMediaPlayer: MediaPlayer? = null

	private val correctAnswerStrings = setOf(
		"Great!",
		"Keep it up!",
		"Wow, you're improving fast",
		"Just think of all the progress you've made!",
		"Fantastic work!",
		"Brilliant job!",
		"Ding!",
		"Yeah!"
	)
	private val wrongAnswerStrings = setOf(
		"Oops, keep trying!",
		"Have another go!",
		"Uh oh, try again!",
		"Almost!",
		"You've got this!",
		"Mistakes are natural",
		"Don't worry, you're doing great!",
		"Oops, try again",
		"Try again",
		"Oops, wrong",
		"Incorrect, this time",
		"Nearly!",
		"Not quite!",
		"Give it another shot",
		"If you can't remember, have a guess",
		"If there's context, look at it and take a guess",
	)

	lateinit var currentWord: SeenWord

	private var endlessCompleted = false

	lateinit var newWordsJsonAdapter: NewWordsJsonAdapter
	lateinit var seenWords: MutableSet<SeenWord>


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, R.layout.learning_activity)
		binding.closeButton.setOnClickListener { finishLevel() }

		// for reading the new words json file at runtime
		newWordsJsonAdapter = NewWordsJsonAdapter(Moshi.Builder().build())

		// read word database
		val newWords = newWordsJsonAdapter.fromJson(readFile(resources, R.raw.new_words))!!.newWords
		Log.d("Init word database", "New words: $newWords")
		val seenNewWords = newWords.take(getPreferences(Context.MODE_PRIVATE).getInt(INDEX_OF_NEXT_NEW_WORD, 0))
		Log.d("Init word database", "Seen new words: $seenNewWords")
		val wordProbabilities = getWordProbabilities(this)
		Log.d("Init word database", "Word probabilities: $wordProbabilities")
		seenWords = seenNewWords.mapIndexed { i, word -> SeenWord(word, wordProbabilities[i]) }.toMutableSet()
		Log.d("Init word database", "Seen words: $seenWords")

		supportFragmentManager.beginTransaction()
			.add(binding.learningActivityArea.id, nextEndlessExercise())
			.commit()
	}


	override fun onStart() {
		super.onStart()
		correctMediaPlayer = MediaPlayer.create(this, R.raw.correct)
		wrongMediaPlayer = MediaPlayer.create(this, R.raw.wrong)
	}


	override fun onStop() {
		super.onStop()
		correctMediaPlayer?.release()
		correctMediaPlayer = null
		wrongMediaPlayer?.release()
		wrongMediaPlayer = null
	}


	fun onCorrect() {
		correctMediaPlayer?.seekTo(0)
		correctMediaPlayer?.start()

		if (nextInt(8) == 0) Snackbar.make(binding.root, correctAnswerStrings.random(), Snackbar.LENGTH_SHORT).setAnchorView(binding.learningActivityArea).show()

		// halve the probability of the word that the user just got correct and save the new probabilities
		currentWord.probability /= 2
		setWordProbabilities(this, seenWords.map { it.probability })
	}


	fun onIncorrect() {
		wrongMediaPlayer?.seekTo(0)
		wrongMediaPlayer?.start()

		if (nextInt(4) == 0) Snackbar.make(binding.root, wrongAnswerStrings.random(), Snackbar.LENGTH_SHORT).setAnchorView(binding.learningActivityArea).show()

		// reset probability of the word that the user just got wrong to half that of new words and save new probabilities
		currentWord.probability = 0.5
		setWordProbabilities(this, seenWords.map { it.probability })
	}


	fun nextScreen() {
		supportFragmentManager.beginTransaction()
			.replace(binding.learningActivityArea.id, nextEndlessExercise())
			.commit()
	}


	private fun nextEndlessExercise(): Fragment {
		if (endlessCompleted || seenWords.sumByDouble { it.probability } > 1f) {
			// present a word that's already been seen

			currentWord = WeightedDice(seenWords.associateBy({it}, {it.probability})).roll()

			return when (val word = currentWord.word) {
				is Noun -> when (nextInt(2)) {
					0 -> GenderFragment(word)
					else -> TranslationFragment(word)
				}

				is WeakPronoun -> WeakPronounFragment(word)

				is Translatable -> TranslationFragment(word)

				else -> throw Exception("Word $word has no practice exercises associated with it.")
			}
		}

		else {
			// try to present a new word

			val indexOfNextNewWord = getPreferences(Context.MODE_PRIVATE).getInt(INDEX_OF_NEXT_NEW_WORD, 0)
			val newWords = newWordsJsonAdapter.fromJson(readFile(resources, R.raw.new_words))!!.newWords

			if (indexOfNextNewWord < newWords.size) {
				// present a new word

				val newWord = newWords[indexOfNextNewWord]

				currentWord = SeenWord(newWord, 1.0)

				return when (newWord) {
					is Translatable -> NewWordFragment(newWord)
					is WeakPronoun -> NewWeakPronounFragment(newWord)
					else -> throw Exception("Word $newWord has no introductory exercises associated with it.")
				}
			}
			else {
				// completed every single word (rare)

				if (!getPreferences(Context.MODE_PRIVATE).getBoolean(ENDLESS_COMPLETED_DIALOG_SEEN, false)) { // only show dialog if it's not been seen yet
					AlertDialog.Builder(this)
						.setTitle("Endless mode completed")
						.setMessage("Congratulations, you have completed endless mode! There are no new words left, but you can still keep practising the words you've seen.")
						.setPositiveButton(android.R.string.ok, null)
						.show()

					with(getPreferences(Context.MODE_PRIVATE).edit()) {
						putBoolean(ENDLESS_COMPLETED_DIALOG_SEEN, true)
						apply()
					}
				}
				endlessCompleted = true
				return nextEndlessExercise()
			}
		}
	}


	private fun finishLevel() {
		finish()
	}
}