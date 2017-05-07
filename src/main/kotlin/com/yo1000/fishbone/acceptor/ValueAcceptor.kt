package com.yo1000.fishbone.acceptor

import com.yo1000.fishbone.expression.IterateExpression
import com.yo1000.fishbone.expression.ViewExpression
import com.yo1000.fishbone.model.Direction
import com.yo1000.fishbone.model.Position
import org.apache.poi.ss.usermodel.Sheet

/**
 *
 * @author yo1000
 */
class ValueAcceptor {
    fun accept(sheet: Sheet, instance: Any, visitor: (Position, Any) -> Unit) {
        val iterateExpressionMap: MutableMap<Position, IterateExpression> = mutableMapOf()
        val viewExpressionMap: MutableMap<Position, ViewExpression> = mutableMapOf()

        CellAcceptor().accept(sheet, { position, expression ->
            if (expression is IterateExpression) {
                iterateExpressionMap.put(position, expression)
            } else if (expression is ViewExpression) {
                viewExpressionMap.put(position, expression)
            }
        })

        val doneSet: MutableSet<Position> = mutableSetOf()

        iterateExpressionMap.forEach { (originRow, originColumn), expression ->
            expression.invoke(instance, { aliasInstance, (relativeRow, relativeColumn), (width, height, direction) ->
                when (direction) {
                    Direction.HORIZONTAL -> {
                        (0..height - 1).forEach eachCell@ {
                            val firstPosition: Position = Position(originRow + it, originColumn)
                            val position: Position = Position(originRow + relativeRow + it,
                                    originColumn + relativeColumn)

                            if (relativeColumn > 0 && viewExpressionMap.containsKey(position)) {
                                return@eachCell
                            }

                            viewExpressionMap[firstPosition]?.invoke(aliasInstance)?.let {
                                doneSet.add(position)
                                visitor(position, it)
                            }
                        }
                    }
                    Direction.VERTICAL -> {
                        (0..width - 1).forEach eachCell@ {
                            val firstPosition: Position = Position(originRow, originColumn + it)
                            val position: Position = Position(originRow + relativeRow,
                                    originColumn + relativeColumn + it)

                            if (relativeRow > 0 && viewExpressionMap.containsKey(position)) {
                                return@eachCell
                            }

                            viewExpressionMap[firstPosition]?.invoke(aliasInstance)?.let {
                                doneSet.add(position)
                                visitor(position, it)
                            }
                        }
                    }
                    else -> Unit
                }
            })
        }

        viewExpressionMap.forEach { position, expression ->
            if (doneSet.contains(position)) {
                return@forEach
            }

            expression.invoke(instance)?.let {
                visitor(position, it)
            }
        }
    }
}