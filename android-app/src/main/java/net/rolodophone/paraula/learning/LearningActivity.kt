package net.rolodophone.paraula.learning

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningActivityBinding
import net.rolodophone.paraula.world.Level
import net.rolodophone.paraula.world.levels

class LearningActivity : AppCompatActivity() {

    companion object {
        const val LEVEL_INDEX_EXTRA = "LEVEL_INDEX"
    }

    lateinit var level: Level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<LearningActivityBinding>(this, R.layout.learning_activity)

        level = levels[intent.getIntExtra(LEVEL_INDEX_EXTRA, -1)]
        binding.level = level

    }

    @Suppress("UNUSED_PARAMETER")
    fun up(view: View) {
        finish()
    }
}