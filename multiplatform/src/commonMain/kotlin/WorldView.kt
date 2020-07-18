package net.rolodophone.paraula.multiplatform

object WorldViewMP {

    enum class Direction { LEFT, TOP, RIGHT, BOTTOM }

    class Level(var left: Level? = null, var top: Level? = null, var right: Level? = null, var bottom: Level? = null) {

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

        fun copy(): Level = Level(left?.copy(), top?.copy(), right?.copy(), bottom?.copy())

        fun isEmpty() = left == null && top == null && right == null && bottom == null
    }

    const val LEVEL_RADIUS = 60f
    const val PATH_LENGTH = 150f

    private val rootLevel = Level(
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


    fun click() {

    }


    fun draw(drawLevel: (x: Float, y: Float) -> Unit, drawPath: (startX: Float, startY: Float, endX: Float, endY: Float) -> Unit) {
        var x = 100f
        var y = 100f
        val levelDirections = mutableListOf<Direction>()
        val editableRootLevel = rootLevel.copy()

        do {
            drawLevel(x, y)

            val level = levelDirections.fold(editableRootLevel) { level: Level, direction: Direction -> level.get(direction)!! }

            level.apply {
                when {
                    left != null -> {
                        drawPath(x, y, x - PATH_LENGTH, y)
                        x -= PATH_LENGTH
                        levelDirections.add(Direction.LEFT)
                    }
                    top != null -> {
                        drawPath(x, y, x, y - PATH_LENGTH)
                        y -= PATH_LENGTH
                        levelDirections.add(Direction.TOP)
                    }
                    right != null -> {
                        drawPath(x, y, x + PATH_LENGTH, y)
                        x += PATH_LENGTH
                        levelDirections.add(Direction.RIGHT)
                    }
                    bottom != null -> {
                        drawPath(x, y, x, y + PATH_LENGTH)
                        y += PATH_LENGTH
                        levelDirections.add(Direction.BOTTOM)
                    }
                    else -> {
                        if (levelDirections.size >= 1) {
                            val parentLevel = levelDirections.dropLast(1).fold(editableRootLevel) { level: Level, direction: Direction -> level.get(direction)!! }
                            parentLevel.remove(levelDirections.last())

                            when (levelDirections.last()) {
                                Direction.LEFT -> x += PATH_LENGTH
                                Direction.TOP -> y += PATH_LENGTH
                                Direction.RIGHT -> x -= PATH_LENGTH
                                Direction.BOTTOM -> y -= PATH_LENGTH
                            }
                            levelDirections.removeAt(levelDirections.size - 1)
                        }
                    }
                }
            }

            log("Drew level at ($x, $y)")

        } while (!editableRootLevel.isEmpty())
    }


    fun measure() {

    }
}