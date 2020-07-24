package net.rolodophone.paraula.learning

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import net.rolodophone.paraula.R

class LearningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learning_activity)
    }

    @Suppress("UNUSED_PARAMETER")
    fun up(view: View) {
        finish()
    }
}