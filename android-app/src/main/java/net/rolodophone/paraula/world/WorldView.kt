package net.rolodophone.paraula.world

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import net.rolodophone.paraula.multiplatform.WorldViewMP

class WorldView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private val levelPaint = Paint()
    private val pathPaint = Paint()

    init {
        levelPaint.color = Color.BLUE
        pathPaint.color = Color.GRAY
    }

    init {
        setOnClickListener(OnClickListener { view: View ->
            WorldViewMP.click()
        })
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        WorldViewMP.draw(
            drawLevel = { x, y -> canvas.drawCircle(x, y, WorldViewMP.LEVEL_RADIUS, levelPaint) },
            drawPath = { startX, startY, endX, endY -> canvas.drawLine(startX, startY, endX, endY, pathPaint) }
        )
    }


//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//
//    }
}