package net.rolodophone.paraula.learning

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
		"Getting stuff wrong is part of the learning process",
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
		"Studies show that near misses improve retention",
		"What a fool! Just kidding, you're doing great :-)"
	)

	private lateinit var currentWord: SeenWord


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, R.layout.learning_activity)
		binding.closeButton.setOnClickListener { finishLevel() }

		specialLevelType = intent.getIntExtra(SPECIAL_LEVEL_EXTRA, -1)

		if (specialLevelType == -1) {
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

		if (nextInt(8) == 0) Snackbar.make(binding.root, correctAnswerStrings.random(), Snackbar.LENGTH_SHORT).show()

		if (specialLevelType == ENDLESS) {
			// halve the probability of the word that the user just got correct and save the new probabilities
			currentWord.probability /= 2
			setWordProbabilities(this, seenWords.map { it.probability })
		}
	}


	fun onIncorrect() {
		wrongMediaPlayer?.seekTo(0)
		wrongMediaPlayer?.start()

		Snackbar.make(binding.root, wrongAnswerStrings.random(), Snackbar.LENGTH_SHORT).show()

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
		if (seenWords.sumByDouble { it.probability } > 1f) { // present a word that's already been seen
			currentWord = WeightedDice(seenWords.associateBy({it}, {it.probability})).roll()

			return when (val word = currentWord.word) {
				is Noun -> when (nextInt(2)) {
					0 -> GenderFragment(word)
					else -> TranslationFragment(Phrase(english = word.english, catalan = word.catalan))
				}
				is Verb -> TranslationFragment(Phrase(english = word.english, catalan = word.catalan))
			}
		}

		else { // present a new word
			val indexOfNextNewWord = getIndexOfNextNewWord(this)
			val newWord = newWordsJsonAdapter.fromJson(readFile(resources, R.raw.new_words))!!.newWords[indexOfNextNewWord]
			setIndexOfNextNewWord(this, indexOfNextNewWord + 1)

			currentWord = SeenWord(newWord, 1.0)
			seenWords.add(currentWord)

			return when (newWord) {
				is Noun -> NewPhraseFragment(Phrase(english = newWord.english, catalan = newWord.catalan))
				is Verb -> NewPhraseFragment(Phrase(english = newWord.english, catalan = newWord.catalan))
			}
		}
	}


	private fun finishLevel() {
		finish()
	}
}