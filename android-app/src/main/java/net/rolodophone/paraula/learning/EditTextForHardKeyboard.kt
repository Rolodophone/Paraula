package net.rolodophone.paraula.learning

import android.content.Context
import android.util.AttributeSet

class EditTextForHardKeyboard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr) {

	override fun onCheckIsTextEditor() = false
}