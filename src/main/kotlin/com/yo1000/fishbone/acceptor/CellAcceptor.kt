package com.yo1000.fishbone.acceptor

import com.yo1000.fishbone.expression.Expression
import com.yo1000.fishbone.expression.IterateExpression
import com.yo1000.fishbone.expression.ViewExpression
import com.yo1000.fishbone.model.Position
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet

/**
 *
 * @author yo1000
 */
class CellAcceptor {
    companion object {
        val CONTENT_REGEX: Regex = Regex("""(\Q{{\E[a-zA-Z0-9_\.\$\s]+\Q}}\E|\Q[[\E[a-zA-Z0-9_\.\$:,\s]+\Q]]\E)""");
    }

    fun accept(sheet: Sheet, visitor: (Position, Expression) -> Unit) {
        (sheet.firstRowNum..sheet.lastRowNum).forEach eachRow@ {
            val row: Row = sheet.getRow(it) ?: sheet.createRow(it)
            if (row.firstCellNum < 0 || row.lastCellNum < 0) {
                return@eachRow
            }

            (row.firstCellNum..row.lastCellNum).forEach eachCell@ {
                val cell: Cell = row.getCell(it) ?: row.createCell(it)
                if (cell.cellTypeEnum == CellType.STRING) {
                    CONTENT_REGEX.findAll(cell.stringCellValue).map { captured -> captured.value }.forEach {
                        if (it.startsWith("{")) {
                            visitor(Position(cell.rowIndex, cell.columnIndex),
                                    ViewExpression(it))
                        } else {
                            visitor(Position(cell.rowIndex, cell.columnIndex),
                                    IterateExpression(it))
                        }
                    }
                }
            }
        }
    }
}