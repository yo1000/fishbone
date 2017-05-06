package com.yo1000.fishbone.expression

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert

/**
 *
 * @author yo1000
 */
object ViewExpressionSpec : Spek({
    data class Stub(
            val abc: String = "ABC"
    )

    given("ViewExpression") {
        val expression = ViewExpression("{{abc}}")

        on("unbracket") {
            val unbracketed = expression.unbracket()

            it("return unbracketed value") {
                Assert.assertEquals("abc", unbracketed)
            }
        }

        on("invoke") {
            val invoked = expression.invoke(Stub())

            it("return expression field value") {
                Assert.assertEquals("ABC", invoked)
            }
        }
    }

    given("ViewExpression when recursive") {
        val expression = ViewExpression("{{i.abc}}")

        on("invoke") {
            val invoked = expression.invoke(mapOf("i" to Stub()))

            it("return expression field value") {
                Assert.assertEquals("ABC", invoked)
            }
        }
    }

    given("ViewExpression when contains whitespace") {
        val expression = ViewExpression("{{ abc }}")

        on("invoke") {
            val invoked = expression.invoke(Stub())

            it("return expression field value") {
                Assert.assertEquals("ABC", invoked)
            }
        }
    }
})
