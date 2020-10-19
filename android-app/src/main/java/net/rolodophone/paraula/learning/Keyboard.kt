package net.rolodophone.paraula.learning

import android.content.Context
import android.os.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.view.inputmethod.*
import android.widget.*
import androidx.core.view.children
import com.google.android.material.card.MaterialCardView
import net.rolodophone.paraula.R
import java.util.*

class Keyboard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), View.OnTouchListener {

	var inputConnection: InputConnection? = null

	var shiftIsPressed = false
	var moreIsPressed = false

	var backspaceIsPressed = false

	private val recursiveChildren: Sequence<View>
		get() = (getChildAt(0) as ViewGroup).children.flatMap { (it as ViewGroup).children }

	private val textInputButtons: Sequence<Button>
		get() = recursiveChildren.filter { it is Button && it.id != R.id.more_button }.map { it as Button }

	private val letters = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m", " ")
	private val symbols = listOf("à", "ç", "é", "è", "í", "ï", "ó", "ò", "ú", "ü", "-", "—", ",", ".", "·", "€", "\"", "(", ")", "«", "»", "'", ":", ";", "!", "?", " ")

	private val backspaceHandler = Handler()
	private val backspaceRunnable = object: Runnable {
		override fun run() {
			backspace()
			backspaceHandler.postDelayed(this, 50)
		}
	}

	init {
		inflate(context, R.layout.keyboard, this) as ViewGroup

		for (child in recursiveChildren) {
			if (child is Button || child is MaterialCardView) {
				child.setOnTouchListener(this)
			}
		}
		for ((i, button) in textInputButtons.withIndex()) {
			button.text = letters[i]
		}
	}

	override fun onTouch(view: View, event: MotionEvent): Boolean {
		when (event.actionMasked) {
			MotionEvent.ACTION_DOWN -> {

				val inputConnection = inputConnection ?: return super.onTouchEvent(event)

				if (view in textInputButtons) {
					inputConnection.commitText((view as Button).text, 1)
					if (shiftIsPressed) shift()
					vibrate()
				}

				else if (view is MaterialCardView || view is Button) {
					when (view.id) {
						R.id.shift_button -> shift()
						R.id.backspace -> {
							backspaceIsPressed = true
							backspace()
							backspaceHandler.postDelayed(backspaceRunnable, 1000)
						}
						R.id.more_button -> more()
						R.id.submit_button -> inputConnection.performEditorAction(EditorInfo.IME_ACTION_DONE)
					}
					vibrate()
				}

				return true
			}

			MotionEvent.ACTION_UP -> {
				backspaceHandler.removeCallbacks(backspaceRunnable)
				return true
			}

			else -> return super.onTouchEvent(event)
		}
	}

	fun backspace() {
		val inputConnection = inputConnection ?: return

		if (TextUtils.isEmpty(inputConnection.getSelectedText(0))) {
			// no text selected so delete previous char
			inputConnection.deleteSurroundingText(1, 0)
		}
		else {
			// replace selected text with empty string
			inputConnection.commitText("", 1)
		}

		vibrate()
	}

	fun shift() {
		shiftIsPressed = !shiftIsPressed

		for (button in textInputButtons) {
			button.text = if (shiftIsPressed) {
				button.text.toString().toUpperCase(Locale.getDefault())
			} else {
				button.text.toString().toLowerCase(Locale.getDefault())
			}
		}
	}

	fun more() {
		moreIsPressed = !moreIsPressed

		var i = 0
		for (button in textInputButtons) {
			button.text = (if (moreIsPressed) symbols else letters)[i]
			i++
		}
	}

	private fun vibrate() {
		val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
		if (Build.VERSION.SDK_INT >= 26) {
			vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
		}
		else {
			@Suppress("DEPRECATION")
			vibrator.vibrate(10)
		}
	}
}