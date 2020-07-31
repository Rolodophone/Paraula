package net.rolodophone.paraula.learning

import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import net.rolodophone.paraula.*
import kotlin.random.Random.Default.nextInt

@Suppress("UNUSED")
class TranslationFragment : Fragment() {

	private lateinit var textView: TextView
	private lateinit var editText: EditText
	private lateinit var progressBar: ProgressBar

	private val vm: TranslationViewModel by lazy { getViewModel { TranslationViewModel((requireActivity() as LearningActivity).level.phrases.iterator()) } }

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
		"Don't worry, you doing great!",
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
		"What a fool! How could you get that wrong? Just kidding, you're doing great :-)"
	)


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		val root = inflater.inflate(R.layout.learning_translation_fragment, container, false) as ViewGroup

		val learningActivity = requireActivity() as LearningActivity

		progressBar = learningActivity.progressBar
		progressBar.max = learningActivity.level.phrases.size

		textView = inflater.inflate(R.layout.learning_translation_text, root, false) as TextView
		vm.textViewText.observe(viewLifecycleOwner) {
			textView.text = it
		}
		root.addView(textView)

		editText = inflater.inflate(R.layout.learning_translation_text_input, root, false) as EditText
		editText.setOnEditorActionListener { _, _, _ -> submitTranslation() }
		root.addView(editText)

		return root
	}


	override fun onStart() {
		super.onStart()
		correctMediaPlayer = MediaPlayer.create(context, R.raw.correct)
		wrongMediaPlayer = MediaPlayer.create(context, R.raw.wrong)
	}


	override fun onStop() {
		super.onStop()
		correctMediaPlayer?.release()
		correctMediaPlayer = null
		wrongMediaPlayer?.release()
		wrongMediaPlayer = null
	}


	private fun submitTranslation(): Boolean {
		if (vm.phrase.translate(editText.text.toString()) == vm.phraseText) {
			//user got it correct

			// play ding sound effect
			correctMediaPlayer?.seekTo(0)
			correctMediaPlayer?.start()

			if (vm.nextTranslationText()) { // try to move to the next phrase, else finish the level
				progressBar.progress++

				editText.text.clear()

				if (nextInt(8) == 0) Snackbar.make(requireView(), correctAnswerStrings.random(), Snackbar.LENGTH_SHORT).show()
			}

			else {
				finishLevel()
			}
		}

		else {
			//user got it wrong

			// play wrong sound effect
			wrongMediaPlayer?.seekTo(0)
			wrongMediaPlayer?.start()

			Snackbar.make(requireView(), wrongAnswerStrings.random(), Snackbar.LENGTH_SHORT).show()
		}

		return true
	}


	private fun finishLevel() {
		requireActivity().finish()
	}
}