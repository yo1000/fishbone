package com.yo1000.fishbone.mapper

import com.yo1000.fishbone.acceptor.SheetAcceptor
import com.yo1000.fishbone.parser.PropertyParser
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.InputStream

/**
 *
 * @author yo1000
 */
class XlsxSheetMapper {
    fun map(workbook: File, instance: Any, containedInstances: Boolean = false,
            doneHandler: (workbook: Workbook) -> Unit) {
        OPCPackage.open(workbook).use {
            val xssfWorkbook = XSSFWorkbook(it)

            SheetAcceptor().accept(xssfWorkbook, { sheet ->
                map(sheet, instance, containedInstances)
            })

            doneHandler(xssfWorkbook)
        }
    }

    fun map(workbook: InputStream, instance: Any, containedInstances: Boolean = false,
            doneHandler: (workbook: Workbook) -> Unit) {
        OPCPackage.open(workbook).use {
            val xssfWorkbook = XSSFWorkbook(it)

            SheetAcceptor().accept(xssfWorkbook, { sheet ->
                map(sheet, instance, containedInstances)
            })

            doneHandler(xssfWorkbook)
        }
    }

    private fun map(sheet: Sheet, instance: Any, containedInstances: Boolean) {
        if (!containedInstances) {
            CellMapper().map(sheet, instance)
            return
        }

        if (instance is Map<*, *>) {
            instance[sheet.sheetName]?.let {
                CellMapper().map(sheet, it)
                return
            }
        }

        PropertyParser(sheet.sheetName).parse(instance)?.let {
            CellMapper().map(sheet, it)
            return
        }
    }
}