package net.rolodophone.paraula.world

import android.content.*
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.children
import net.rolodophone.paraula.*
import net.rolodophone.paraula.learning.*

class WorldLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

	// LEVEL BUTTONS

	override fun onFinishInflate() {
		super.onFinishInflate()

		for (level in levels) {
			inflate(context, R.layout.level_button, this)
		}

		for ((i, child) in children.withIndex()) {

			// make button smaller when clicked
			child.setOnTouchListener { view, event ->

				val x = levels[i].x.toPx(context)
				val y = levels[i].y.toPx(context)

				when (event.action) {

					MotionEvent.ACTION_DOWN -> {

						val s = Level.SMALL_RADIUS_DP.toPx(context)
						child.layout(x-s, y-s, x+s, y+s)
					}

					MotionEvent.ACTION_UP -> {

						val s = Level.RADIUS_DP.toPx(context)
						child.layout(x-s, y-s, x+s, y+s)

						performClick()

						val intent = Intent(context, LearningActivity::class.java)
						intent.putExtra(LearningActivity.LEVEL_INDEX_EXTRA, indexOfChild(view))
						context.startActivity(intent)
					}
				}

				true
			}
		}
	}

	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
		for ((i, child) in children.withIndex()) {
			val level = levels[i]
			val x = level.x.toPx(context)
			val y = level.y.toPx(context)
			val s = Level.RADIUS_DP.toPx(context)
			child.layout(x-s, y-s, x+s, y+s)
		}
	}


	// BACKGROUND IMAGE

	private val backgroundImage: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_world_background)!!

	init {
		backgroundImage.setBounds(0, 0, backgroundImage.intrinsicWidth, backgroundImage.intrinsicHeight)
		setWillNotDraw(false)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		setMeasuredDimension(backgroundImage.intrinsicWidth, backgroundImage.intrinsicHeight)
	}

	override fun onDraw(canvas: Canvas) {
		backgroundImage.draw(canvas)
	}
}