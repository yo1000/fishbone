package com.yo1000.fishbone.model

/**
 *
 * @author yo1000
 */
enum class Direction(
        val label: String
) {
    VERTICAL("V"),
    HORIZONTAL("H"),
    NONE("");

    companion object {
        fun labelOf(label: String): com.yo1000.fishbone.model.Direction {
            return com.yo1000.fishbone.model.Direction.values().filter { it.label == label }.getOrElse(0, defaultValue = { com.yo1000.fishbone.model.Direction.NONE })
        }
    }
}
