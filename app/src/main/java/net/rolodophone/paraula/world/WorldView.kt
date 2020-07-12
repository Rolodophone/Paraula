package net.rolodophone.paraula.world

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import net.rolodophone.paraula.R

class WorldView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    companion object {
        const val LEVEL_RADIUS = 32
        const val PATH_LENGTH = 64
    }


}