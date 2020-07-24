package net.rolodophone.paraula.learning

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.color
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R

@Suppress("UNUSED")
class TranslationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.learning_translation_fragment, container, false) as ViewGroup

        val textView = inflater.inflate(R.layout.learning_translation_text, root, false) as TextView
        textView.text = SpannableStringBuilder()
            .append("This is the ")
            .bold { color(Color.WHITE) { append("text") } }
            .append(" to be translated")
        root.addView(textView)

        inflater.inflate(R.layout.learning_translation_text_input, root, true)

        return root
    }
}