package com.yo1000.fishbone.acceptor

import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

/**
 *
 * @author yo1000
 */
class SheetAcceptor {
    fun accept(workbook: Workbook, visitor: (sheet: Sheet) -> Unit) {
        workbook.forEach {
            it?.let {
                visitor(it)
            }
        }
    }
}