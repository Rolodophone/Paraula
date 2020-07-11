package net.rolodophone.paraula.world

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import net.rolodophone.paraula.R

class WorldLevel @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    companion object {
        const val SIZE = 64
    }

    private val paint = Paint()

    val nextLevelLeftRef: Int
    val nextLevelUpRef: Int
    val nextLevelRightRef: Int
    val nextLevelDownRef: Int

    var nextLevelLeft: WorldLevel? = null
    var nextLevelUp: WorldLevel? = null
    var nextLevelRight: WorldLevel? = null
    var nextLevelDown: WorldLevel? = null

    init {
        paint.color = ContextCompat.getColor(context, R.color.colorAccent)

        context.theme.obtainStyledAttributes(attrs, R.styleable.WorldLevel, 0, 0).apply {
            try {
                nextLevelLeftRef = getResourceId(R.styleable.WorldLevel_nextLevelLeft, 0)
                nextLevelUpRef = getResourceId(R.styleable.WorldLevel_nextLevelUp, 0)
                nextLevelRightRef = getResourceId(R.styleable.WorldLevel_nextLevelRight, 0)
                nextLevelDownRef = getResourceId(R.styleable.WorldLevel_nextLevelDown, 0)
            }
            finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(SIZE, SIZE)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(left + width/2f, top + height/2f, width/2f, paint)
    }
}