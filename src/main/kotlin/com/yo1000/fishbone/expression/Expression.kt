package com.yo1000.fishbone.expression

/**
 *
 * @author yo1000
 */
interface Expression {
    companion object {
        val BRACKET_REGEX: Regex = Regex("""(?:^\Q{{\E\s*|^\Q[[\E\s*|\s*\Q}}\E$|\s*\Q]]\E$)""")
    }

    val source: String

    fun unbracket(): String {
        return BRACKET_REGEX.replace(source, "")
    }
}