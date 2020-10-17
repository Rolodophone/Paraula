package net.rolodophone.paraula.learning

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.view.inputmethod.InputConnection
import android.widget.*
import androidx.core.view.children
import com.google.android.material.card.MaterialCardView
import net.rolodophone.paraula.R
import java.util.*

class Keyboard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), View.OnTouchListener {

	var inputConnection: InputConnection? = null
	var shiftIsPressed = false

	private val buttonViews: Sequence<View>
		get() = (getChildAt(0) as ViewGroup).children.flatMap { (it as ViewGroup).children }

	init {
		inflate(context, R.layout.keyboard, this) as ViewGroup

		for (button in buttonViews) {
			if (button is Button || button is MaterialCardView) {
				button.setOnTouchListener(this)
			}
		}
	}

	override fun onTouch(view: View, event: MotionEvent): Boolean {
		if (event.actionMasked != MotionEvent.ACTION_DOWN) return super.onTouchEvent(event)

		val inputConnection = inputConnection ?: return super.onTouchEvent(event)

		//vibrate
		performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)

		if (view is Button) {
			inputConnection.commitText(view.text, 1)
		}

		else if (view is MaterialCardView) {

			if (view.id == R.id.shift_button) {

				for (child in buttonViews) {
					if (child is Button) {

						child.text = if (shiftIsPressed) {
							child.text.toString().toLowerCase(Locale.getDefault())
						} else {
							child.text.toString().toUpperCase(Locale.getDefault())
						}
					}
				}

				shiftIsPressed = !shiftIsPressed
			}

			else if (view.id == R.id.backspace) {
				if (TextUtils.isEmpty(inputConnection.getSelectedText(0))) {
					// no text selected so delete previous char
					inputConnection.deleteSurroundingText(1, 0)
				}
				else {
					// replace selected text with empty string
					inputConnection.commitText("", 1)
				}
			}
		}

		return true
	}
}