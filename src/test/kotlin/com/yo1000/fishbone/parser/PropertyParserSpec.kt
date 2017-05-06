package com.yo1000.fishbone.parser

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert

/**
 *
 * @author yo1000
 */
object PropertyParserSpec : Spek({
    data class StubInner(
            val fieldString: String,
            val fieldInt: Int
    )

    data class StubOuter(
            val stubInner: StubInner
    )

    given("PropertyParser") {
        val stubInstance = StubOuter(StubInner("abc", 123))

        on("parse string field from object") {
            val parsed = PropertyParser("stubInner.fieldString").parse(stubInstance)

            it("return string value") {
                Assert.assertEquals("abc", parsed)
            }
        }

        on("parse int field from object") {
            val parsed = PropertyParser("stubInner.fieldInt").parse(stubInstance)

            it("return int value") {
                Assert.assertEquals(123, parsed)
            }
        }

        on("parse str field from map") {
            val parsed = PropertyParser("i.fieldString").parse(mapOf("i" to StubInner("abc", 123)))

            it("return int value") {
                Assert.assertEquals("abc", parsed)
            }
        }

        on("parse int field from map") {
            val parsed = PropertyParser("i.fieldInt").parse(mapOf("i" to StubInner("abc", 123)))

            it("return int value") {
                Assert.assertEquals(123, parsed)
            }
        }
    }
})
