package com.yo1000.fishbone.mapper

import com.yo1000.fishbone.acceptor.ValueAcceptor
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import java.util.*

/**
 *
 * @author yo1000
 */
class CellMapper {
    fun map(sheet: Sheet, instance: Any) {
        ValueAcceptor().accept(sheet, instance, { (rowNum, column), value ->
            val row: Row = sheet.getRow(rowNum) ?: sheet.createRow(rowNum)
            val cell: Cell = row.getCell(column) ?: row.createCell(column)

            when (value) {
                is String -> {
                    cell.setCellValue(value)
                }
                is Number -> {
                    cell.setCellValue(value.toDouble())
                }
                is Boolean -> {
                    cell.setCellValue(value)
                }
                is Date -> {
                    cell.setCellValue(value)
                }
                else -> Unit
            }
        })
    }
}