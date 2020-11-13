package net.rolodophone.paraula.learning

import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import net.rolodophone.paraula.*
import net.rolodophone.paraula.databinding.LearningActivityBinding
import org.nield.kotlinstatistics.WeightedDice
import kotlin.random.Random.Default.nextInt

class LearningActivity : AppCompatActivity() {

	companion object {
		const val LEVEL_INDEX_EXTRA = "LEVEL_INDEX"  // -1 means no level
		const val SPECIAL_LEVEL_EXTRA = "SPECIAL_LEVEL"

		const val NONE = -1
		const val ENDLESS = 0

		const val INDEX_OF_NEXT_NEW_WORD = "INDEX_OF_NEXT_NEW_WORD"
		const val ENDLESS_COMPLETED_DIALOG_SEEN = "ENDLESS_COMPLETED_DIALOG_SEEN"
	}

	private lateinit var level: Level
	private var specialLevelType: Int = NONE
	private lateinit var exerciseIterator: Iterator<Fragment>

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

	private lateinit var currentWord: SeenWord

	private var endlessCompleted = false

	private lateinit var newWordsJsonAdapter: NewWordsJsonAdapter
	private lateinit var seenWords: MutableSet<SeenWord>


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, R.layout.learning_activity)
		binding.closeButton.setOnClickListener { finishLevel() }

		specialLevelType = intent.getIntExtra(SPECIAL_LEVEL_EXTRA, -1)

		if (specialLevelType == NONE) {
			level = levels[intent.getIntExtra(LEVEL_INDEX_EXTRA, -1)]

			if (level is WipLevel) {
				Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show()
				finish()
				return
			}

			val exercises = level.getExercises()
			exerciseIterator = exercises.iterator()

			binding.learningProgress.max = exercises.size

			supportFragmentManager.beginTransaction()
				.add(binding.learningActivityArea.id, exerciseIterator.next())
				.commit()
		}

		else {
			binding.learningProgress.max = 100

			// for reading the new words json file at runtime
			newWordsJsonAdapter = NewWordsJsonAdapter(moshi)

			val newWords = newWordsJsonAdapter.fromJson(readFile(resources, R.raw.new_words))!!.newWords
			val seenNewWords = newWords.take(getPreferences(Context.MODE_PRIVATE).getInt(INDEX_OF_NEXT_NEW_WORD, 0))
			val wordProbabilities = getWordProbabilities(this)
			seenWords = seenNewWords.mapIndexed { i, word -> SeenWord(word, wordProbabilities[i]) }.toMutableSet()

			supportFragmentManager.beginTransaction()
				.add(binding.learningActivityArea.id, nextEndlessExercise())
				.commit()
		}
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

		if (specialLevelType == ENDLESS) {
			// halve the probability of the word that the user just got correct and save the new probabilities
			currentWord.probability /= 2
			setWordProbabilities(this, seenWords.map { it.probability })
		}
	}


	fun onIncorrect() {
		wrongMediaPlayer?.seekTo(0)
		wrongMediaPlayer?.start()

		if (nextInt(4) == 0) Snackbar.make(binding.root, wrongAnswerStrings.random(), Snackbar.LENGTH_SHORT).setAnchorView(binding.learningActivityArea).show()

		if (specialLevelType == ENDLESS) {
			// reset probability of the word that the user just got wrong to half that of new words and save new probabilities
			currentWord.probability = 0.5
			setWordProbabilities(this, seenWords.map { it.probability })
		}
	}


	fun nextScreen() {
		when (specialLevelType) {
			NONE -> { // world level
				if (exerciseIterator.hasNext()) {

					binding.learningProgress.progress++

					supportFragmentManager.beginTransaction()
						.replace(binding.learningActivityArea.id, exerciseIterator.next())
						.commit()
				}

				else {
					finishLevel()
				}
			}

			ENDLESS -> {
				supportFragmentManager.beginTransaction()
					.replace(binding.learningActivityArea.id, nextEndlessExercise())
					.commit()

				// flash progress bar
				binding.learningProgress.progress = 0
				GlobalScope.launch {
					repeat(100) {
						binding.learningProgress.progress++
						delay(1L)
					}
					delay(1000L)
					binding.learningProgress.progress = 0
				}
			}
		}
	}

	private fun nextEndlessExercise(): Fragment {
		if (seenWords.sumByDouble { it.probability } > 1f || endlessCompleted) { // present a word that's already been seen
			currentWord = WeightedDice(seenWords.associateBy({it}, {it.probability})).roll()

			return when (val word = currentWord.word) {
				is Noun -> when (nextInt(2)) {
					0 -> GenderFragment(word)
					else -> TranslationFragment(Phrase(english = word.english, catalan = word.catalan))
				}
				is Verb -> TranslationFragment(Phrase(english = word.english, catalan = word.catalan))
				is StressedPronoun -> TranslationFragment(Phrase(english = word.getEnglishExample(seenWords), catalan = word.getCatalanExample(seenWords)))
			}
		}

		else { // present a new word
			val indexOfNextNewWord = getPreferences(Context.MODE_PRIVATE).getInt(INDEX_OF_NEXT_NEW_WORD, 0)
			val newWords = newWordsJsonAdapter.fromJson(readFile(resources, R.raw.new_words))!!.newWords

			if (indexOfNextNewWord < newWords.size) {
				val newWord = newWords[indexOfNextNewWord]

				with(getPreferences(Context.MODE_PRIVATE).edit()) {
					putInt(INDEX_OF_NEXT_NEW_WORD, indexOfNextNewWord + 1)
					putBoolean(ENDLESS_COMPLETED_DIALOG_SEEN, false) // if this has been set to true, reset to false because I must've added more new words
					apply()
				}

				currentWord = SeenWord(newWord, 1.0)
				seenWords.add(currentWord)

				return when (newWord) {
					is Noun 	-> NewPhraseFragment(Phrase(english = newWord.english, catalan = newWord.catalan))
					is Verb 	-> NewPhraseFragment(Phrase(english = newWord.english, catalan = newWord.catalan))
					is StressedPronoun 	-> //TODO
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