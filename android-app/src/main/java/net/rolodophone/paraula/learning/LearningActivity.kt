package net.rolodophone.paraula.learning

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningActivityBinding
import net.rolodophone.paraula.world.*

class LearningActivity : AppCompatActivity() {

    companion object {
        const val LEVEL_INDEX_EXTRA = "LEVEL_INDEX"
    }

    lateinit var level: Level
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<LearningActivityBinding>(this, R.layout.learning_activity)

        progressBar = binding.learningProgress

        level = levels[intent.getIntExtra(LEVEL_INDEX_EXTRA, -1)]
    }

    @Suppress("UNUSED_PARAMETER")
    fun up(view: View) {
        finish()
    }
}