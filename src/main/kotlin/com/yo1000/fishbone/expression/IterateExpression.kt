package com.yo1000.fishbone.expression

import com.yo1000.fishbone.model.Direction
import com.yo1000.fishbone.model.Position
import com.yo1000.fishbone.model.Range
import com.yo1000.fishbone.parser.PropertyParser

/**
 * [[list : i : range]]
 *
 * @author yo1000
 */
class IterateExpression(
        override val source: String
) : Expression {
    companion object {
        val VALIDATION_REGEX: Regex = Regex("""[\s]*[a-zA-Z0-9_\$]+[\s]*:[\s]*[a-zA-Z0-9_\.\$]+[\s]*:[\s]*[0-9]+[\s]*,[\s]*[0-9]+[\s]*,[\s]*[HV]{1}[\s]*""")

        private const val SYNTAX_ELEMENT_INDEX: Int = 0
        private const val SYNTAX_LIST_INDEX: Int = 1
        private const val SYNTAX_RANGE_INDEX: Int = 2

        private const val RANGE_WIDTH_INDEX: Int = 0
        private const val RANGE_HEIGHT_INDEX: Int = 1
        private const val RANGE_DIRECTION_INDEX: Int = 2
    }

    fun invoke(instance: Any, visitor: (Any, Position, Range) -> Unit) {
        val unbracketed: String = unbracket()
        validate(unbracketed)

        val syntaxes: List<String> = unbracketed.replace(Regex("""\s+"""), "").split(":")
        val elementName: String = syntaxes[SYNTAX_ELEMENT_INDEX]
        val listProperty: String = syntaxes[SYNTAX_LIST_INDEX]
        val rangeArgs: List<String> = syntaxes[SYNTAX_RANGE_INDEX].split(",")
        val range: Range = Range(rangeArgs[RANGE_WIDTH_INDEX].toInt(), rangeArgs[RANGE_HEIGHT_INDEX].toInt(),
                Direction.labelOf(rangeArgs[RANGE_DIRECTION_INDEX]))

        PropertyParser(listProperty).parse(instance)?.let {
            if (it !is List<*>) {
                return
            }

            val list = it

            when (range.direction) {
                Direction.VERTICAL -> {
                    val lastIndex: Int = if (list.size < range.height) list.lastIndex else range.height - 1

                    (0..lastIndex).forEach {
                        visitor(mapOf(elementName to list[it]),
                                Position(it, 0),
                                Range(range.width, 1, range.direction)
                        )
                    }
                }
                Direction.HORIZONTAL -> {
                    val lastIndex: Int = if (list.size < range.width) list.lastIndex else range.width - 1

                    (0..lastIndex).forEach {
                        visitor(mapOf(elementName to list[it]),
                                Position(0, it),
                                Range(1, range.height, range.direction)
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    private fun validate(expression: String) {
        if (!VALIDATION_REGEX.matches(expression)) {
            throw IllegalExpressionException("`$expression` is invalid format")
        }
    }

    override fun toString(): String {
        return "IterateExpression(source='$source')"
    }
}
