package com.yo1000.fishbone.mapper

import org.apache.poi.xssf.usermodel.XSSFWorkbook
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
object CellMapperSpec : Spek({
    data class Stub(
            val str: String = "ABC",
            val int: Int = 100,
            val date: Date = Date.from(Instant.parse("2017-05-05T02:15:00.0Z"))
    )

    given("CellMapper") {
        val cellMapper = CellMapper()
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("sheet1")

        sheet.createRow(10).createCell(10).setCellValue("{{ i.str }} [[ i : items : 3,5,V ]]")
        sheet.getRow(10).createCell(11).setCellValue("{{ i.int }}")
        sheet.getRow(10).createCell(12).setCellValue("{{ i.date }}")
        sheet.createRow(12).createCell(10).setCellValue("{{ str }}")

        on("mapping map") {
            cellMapper.map(sheet, mapOf(
                    "items" to listOf(
                            Stub(), Stub(), Stub(), Stub(),
                            Stub(), Stub(), Stub(), Stub()),
                    "str" to "XYZ"))

            it("mapped cells") {
                Assert.assertEquals(Stub().str, sheet.getRow(10).getCell(10).stringCellValue)
                Assert.assertEquals(Stub().int.toDouble(), sheet.getRow(10).getCell(11).numericCellValue, 0.0)
                Assert.assertEquals(Stub().date, sheet.getRow(10).getCell(12).dateCellValue)

                Assert.assertEquals(Stub().str, sheet.getRow(11).getCell(10).stringCellValue)
                Assert.assertEquals(Stub().int.toDouble(), sheet.getRow(11).getCell(11).numericCellValue, 0.0)
                Assert.assertEquals(Stub().date, sheet.getRow(11).getCell(12).dateCellValue)

                Assert.assertEquals("XYZ", sheet.getRow(12).getCell(10).stringCellValue)
                Assert.assertEquals(Stub().int.toDouble(), sheet.getRow(12).getCell(11).numericCellValue, 0.0)
                Assert.assertEquals(Stub().date, sheet.getRow(12).getCell(12).dateCellValue)

                Assert.assertEquals(Stub().str, sheet.getRow(13).getCell(10).stringCellValue)
                Assert.assertEquals(Stub().int.toDouble(), sheet.getRow(13).getCell(11).numericCellValue, 0.0)
                Assert.assertEquals(Stub().date, sheet.getRow(13).getCell(12).dateCellValue)

                Assert.assertEquals(Stub().str, sheet.getRow(14).getCell(10).stringCellValue)
                Assert.assertEquals(Stub().int.toDouble(), sheet.getRow(14).getCell(11).numericCellValue, 0.0)
                Assert.assertEquals(Stub().date, sheet.getRow(14).getCell(12).dateCellValue)

                Assert.assertNull(sheet.getRow(15))
            }
        }
    }
})
