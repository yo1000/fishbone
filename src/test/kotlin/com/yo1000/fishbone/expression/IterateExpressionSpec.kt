package com.yo1000.fishbone.expression

import com.yo1000.fishbone.model.Direction
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import java.time.Instant
import java.util.*

/**
 *
 * @author yo1000
 */
object IterateExpressionSpec : Spek({
    data class Stub(
            val str: String = "ABC",
            val int: Int = 100,
            val date: Date = Date.from(Instant.parse("2017-05-05T02:15:00.0Z"))
    )

    data class StubListContainer(
            val item: List<Stub> = listOf(
                    Stub(),
                    Stub()
            )
    )

    given("IterateExpression") {
        val expression = IterateExpression("[[i : item : 3,5,V]]")

        on("unbracket") {
            val unbracketed = expression.unbracket()

            it("return unbracketed value") {
                Assert.assertEquals("i : item : 3,5,V", unbracketed)
            }
        }

        on("invoke with model") {
            var count = 0

            expression.invoke(StubListContainer(), visitor = { instance, position, range ->

                it("visit expression instance at $position") {
                    if (instance !is Map<*, *>) {
                        Assert.fail("instance type is `${instance::class}`")
                        return@it
                    }

                    Assert.assertEquals(Stub(), instance["i"])

                    Assert.assertEquals(count++, position.row)
                    Assert.assertEquals(0, position.column)

                    Assert.assertEquals(Direction.VERTICAL, range.direction)
                    Assert.assertEquals(3, range.width)
                    Assert.assertEquals(1, range.height)
                }
            })

            it("count visited") {
                Assert.assertEquals(2, count)
            }
        }

        on("invoke with map with 2 list") {
            var count = 0

            expression.invoke(mapOf("item" to listOf(
                    Stub(), Stub()
            )), visitor = { instance, position, range ->

                it("visit expression instance at $position") {
                    if (instance !is Map<*, *>) {
                        Assert.fail("instance type is `${instance::class}`")
                        return@it
                    }

                    Assert.assertEquals(Stub(), instance["i"])

                    Assert.assertEquals(count++, position.row)
                    Assert.assertEquals(0, position.column)

                    Assert.assertEquals(Direction.VERTICAL, range.direction)
                    Assert.assertEquals(3, range.width)
                    Assert.assertEquals(1, range.height)
                }
            })

            it("count visited") {
                Assert.assertEquals(2, count)
            }
        }

        on("invoke with map with 8 list") {
            var count = 0

            expression.invoke(mapOf("item" to listOf(
                    Stub(), Stub(), Stub(), Stub(),
                    Stub(), Stub(), Stub(), Stub()
            )), visitor = { instance, position, range ->

                it("visit expression instance at $position") {
                    if (instance !is Map<*, *>) {
                        Assert.fail("instance type is `${instance::class}`")
                        return@it
                    }

                    Assert.assertEquals(Stub(), instance["i"])

                    Assert.assertEquals(count++, position.row)
                    Assert.assertEquals(0, position.column)

                    Assert.assertEquals(Direction.VERTICAL, range.direction)
                    Assert.assertEquals(3, range.width)
                    Assert.assertEquals(1, range.height)
                }
            })

            it("count visited") {
                Assert.assertEquals(5, count)
            }
        }
    }
})
