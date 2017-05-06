package com.yo1000.fishbone.expression

import com.yo1000.fishbone.parser.PropertyParser

/**
 * {{item}}
 *
 * @author yo1000
 */
class ViewExpression(
        override val source: String
) : Expression {
    fun invoke(instance: Any): Any? {
        return PropertyParser(unbracket()).parse(instance)
    }

    override fun toString(): String {
        return "ViewExpression(source='$source')"
    }
}