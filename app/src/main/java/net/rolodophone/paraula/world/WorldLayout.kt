package net.rolodophone.paraula.world

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

class WorldLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        private const val PATH_LENGTH = 64

        private val pathPaint = Paint()
        init { pathPaint.color = Color.GRAY }
    }


//    class LayoutParams: ViewGroup.LayoutParams {
//        constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
//        constructor(width: Int, height: Int): super(width, height)
//        constructor(source: ViewGroup.LayoutParams): super(source)
//
//        //makes layout_width and layout_height optional and ignores them
//        override fun setBaseAttributes(a: TypedArray, widthAttr: Int, heightAttr: Int) {
//            width = WRAP_CONTENT
//            height = WRAP_CONTENT
//        }
//    }

    override fun generateDefaultLayoutParams() = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        for (child in children) {
            if (child is WorldLevel) {
                child.nextLevelLeft = findViewById(child.nextLevelLeftRef)
                child.nextLevelUp = findViewById(child.nextLevelUpRef)
                child.nextLevelRight = findViewById(child.nextLevelRightRef)
                child.nextLevelDown = findViewById(child.nextLevelDownRef)
            }
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        getChildAt(0).apply {
            layout(left, top, left + WorldLevel.SIZE, top + WorldLevel.SIZE)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // TODO
    }


    override fun onDraw(canvas: Canvas) {
        for (child in children) {
            if (child is WorldLevel) {  // for smart casting

                child.draw(canvas)

                child.nextLevelLeft.also  { if (it != null) canvas.drawLine(child.leftF,  child.yMid,    it.rightF, it.yMid,    pathPaint) }
                child.nextLevelUp.also    { if (it != null) canvas.drawLine(child.xMid,   child.topF,    it.xMid,   it.bottomF, pathPaint) }
                child.nextLevelRight.also { if (it != null) canvas.drawLine(child.rightF, child.yMid,    it.leftF,  it.yMid,    pathPaint) }
                child.nextLevelDown.also  { if (it != null) canvas.drawLine(child.xMid,   child.bottomF, it.xMid,   it.topF,    pathPaint) }
            }
        }
    }
}

val View.xMid get() = left + width / 2f
val View.yMid get() = top + height / 2f
val View.leftF get() = left.toFloat()
val View.topF get() = top.toFloat()
val View.rightF get() = right.toFloat()
val View.bottomF get() = bottom.toFloat()