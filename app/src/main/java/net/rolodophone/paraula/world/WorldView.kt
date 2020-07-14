package net.rolodophone.paraula.world

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnClickListener

class WorldView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    class Level(var left: Level? = null, var top: Level? = null, var right: Level? = null, var bottom: Level? = null) {

        enum class Direction { LEFT, TOP, RIGHT, BOTTOM }

        fun get(direction: Direction) = when (direction) {
            Direction.LEFT -> left
            Direction.TOP -> top
            Direction.RIGHT -> right
            Direction.BOTTOM -> bottom
        }

        fun remove(direction: Direction) {
            when (direction) {
                Direction.LEFT -> left = null
                Direction.TOP -> top = null
                Direction.RIGHT -> right = null
                Direction.BOTTOM -> bottom = null
            }
        }
    }

    private companion object {
        const val LEVEL_RADIUS = 30f
        const val PATH_LENGTH = 60f

        val levelPaint = Paint()
        val pathPaint = Paint()

        init {
            levelPaint.color = Color.BLUE
            pathPaint.color = Color.GRAY
        }

        val rootLevel = Level(
            right = Level(),
            bottom = Level(
                left = Level(),
                bottom = Level(
                    left = Level(),
                    right = Level(),
                    bottom = Level()
                )
            )
        )
    }

    init {
        setOnClickListener(OnClickListener { view: View ->

        })
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        var x = 0f
        var y = 0f
        val levelDirections = mutableListOf<Level.Direction>()
        val editableRootLevel = rootLevel

        do {
            canvas.drawCircle(x, y, LEVEL_RADIUS, levelPaint)

            val level = levelDirections.fold(editableRootLevel) { level: Level, direction: Level.Direction -> level.get(direction)!! }

            level.apply {
                when {
                    left != null -> {
                        canvas.drawLine(x, y, x - PATH_LENGTH, y, pathPaint)
                        x -= PATH_LENGTH
                        levelDirections.add(Level.Direction.LEFT)
                    }
                    top != null -> {
                        canvas.drawLine(x, y, x, y - PATH_LENGTH, pathPaint)
                        y -= PATH_LENGTH
                        levelDirections.add(Level.Direction.TOP)
                    }
                    right != null -> {
                        canvas.drawLine(x, y, x + PATH_LENGTH, y, pathPaint)
                        x += PATH_LENGTH
                        levelDirections.add(Level.Direction.RIGHT)
                    }
                    bottom != null -> {
                        canvas.drawLine(x, y, x, y + PATH_LENGTH, pathPaint)
                        y += PATH_LENGTH
                        levelDirections.add(Level.Direction.BOTTOM)
                    }
                    else -> {
                        if (levelDirections.size >= 1) {
                            val parentLevel = levelDirections.dropLast(1).fold(editableRootLevel) { level: Level, direction: Level.Direction -> level.get(direction)!! }
                            parentLevel.remove(levelDirections.last())
                            levelDirections.removeAt(levelDirections.size - 1)
                        }
                    }
                }
            }

            Log.v("WorldView", "Drew level at ($x, $y)")

        } while (levelDirections.size != 0)
    }


//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//
//    }
}