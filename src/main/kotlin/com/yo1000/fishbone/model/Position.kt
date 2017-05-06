package com.yo1000.fishbone.model

/**
 *
 * @author yo1000
 */
data class Position(
        val row: Int,
        val column: Int
) {
    override fun hashCode(): Int {
        return 4001 xor row xor column
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Position) return false

        return row == other.row && column == other.column
    }
}
