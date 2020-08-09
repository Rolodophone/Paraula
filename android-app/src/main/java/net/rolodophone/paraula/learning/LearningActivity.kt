package net.rolodophone.paraula.learning

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import net.rolodophone.paraula.*
import net.rolodophone.paraula.databinding.LearningActivityBinding
import kotlin.random.Random

class LearningActivity : AppCompatActivity() {

	companion object {
		const val LEVEL_INDEX_EXTRA = "LEVEL_INDEX"
	}

	private lateinit var level: Level
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


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		level = levels[intent.getIntExtra(LEVEL_INDEX_EXTRA, -1)]

		if (level is WipLevel) {
			Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show()
			finish()
			return
		}

		val exercises = level.getExercises()
		exerciseIterator = exercises.iterator()

		binding = DataBindingUtil.setContentView(this, R.layout.learning_activity)
		binding.learningProgress.max = exercises.size
		binding.closeButton.setOnClickListener { finishLevel() }

		supportFragmentManager.beginTransaction()
			.add(binding.learningActivityArea.id, exerciseIterator.next())
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

		if (Random.nextInt(8) == 0) Snackbar.make(binding.root, correctAnswerStrings.random(), Snackbar.LENGTH_SHORT).show()
	}


	fun onIncorrect() {
		wrongMediaPlayer?.seekTo(0)
		wrongMediaPlayer?.start()

		Snackbar.make(binding.root, wrongAnswerStrings.random(), Snackbar.LENGTH_SHORT).show()
	}


	fun nextScreen() {
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


	fun finishLevel() {
		finish()
	}
}