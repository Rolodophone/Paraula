package net.rolodophone.paraula.world

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import net.rolodophone.paraula.R

class WorldLevel(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {
    private val paint = Paint()
    init {
        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(64, 64)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(left + width/2f, top + height/2f, width/2f, paint)
    }
}