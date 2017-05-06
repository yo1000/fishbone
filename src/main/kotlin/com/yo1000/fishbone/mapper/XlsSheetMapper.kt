package com.yo1000.fishbone.mapper

import com.yo1000.fishbone.acceptor.SheetAcceptor
import com.yo1000.fishbone.parser.PropertyParser
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.InputStream

/**
 *
 * @author yo1000
 */
class XlsSheetMapper {
    fun map(workbook: File, instance: Any, containedInstances: Boolean = false,
            doneHandler: (workbook: Workbook) -> Unit) {
        POIFSFileSystem(workbook).use {
            val hssfWorkbook = HSSFWorkbook(it)

            SheetAcceptor().accept(hssfWorkbook, { sheet ->
                map(sheet, instance, containedInstances)
            })

            doneHandler(hssfWorkbook)
        }
    }

    fun map(workbook: InputStream, instance: Any, containedInstances: Boolean = false,
            doneHandler: (workbook: Workbook) -> Unit) {
        POIFSFileSystem(workbook).use {
            val hssfWorkbook = HSSFWorkbook(it)

            SheetAcceptor().accept(hssfWorkbook, { sheet ->
                map(sheet, instance, containedInstances)
            })

            doneHandler(hssfWorkbook)
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