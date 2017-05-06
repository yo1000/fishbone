package com.yo1000.fishbone.expression

/**
 *
 * @author yo1000
 */
class IllegalExpressionException(
        override val message: String
) : IllegalArgumentException(message)