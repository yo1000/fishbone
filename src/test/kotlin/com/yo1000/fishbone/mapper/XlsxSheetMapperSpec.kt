package com.yo1000.fishbone.mapper
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author yo1000
 */
object XlsxSheetMapperSpec : Spek({
    data class SalesStatement(
            val name: String,
            val price: Int,
            val number: Int,
            val subtotal: Int = price * number
    )

    data class Sales(
            val subject: String,
            val date: Date,
            val items: List<SalesStatement>,
            val totalNumber: Int = items.sumBy { it.number },
            val totalFee: Int = items.sumBy { it.subtotal }
    )

    given("XlsxSheetMapper") {
        val sheetMapper = XlsxSheetMapper()
        val sales = Sales(
                "Garden center",
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-05-07 01:54:40"),
                listOf(
                        SalesStatement("Matsuba-giku white", 220, 1),
                        SalesStatement("Matsuba-giku purple", 220, 1),
                        SalesStatement("Matsuba-giku yellow", 220, 1),
                        SalesStatement("Matsuba-giku orange", 220, 1),
                        SalesStatement("Cattleya clover", 450, 1),
                        SalesStatement("Shiba-zakura", 200, 2),
                        SalesStatement("Hime-shakunage blue-ice", 650, 1),
                        SalesStatement("Rosemary", 270, 1)
                )
        )

        on("mapping sheet") {
            sheetMapper.map(this::class.java.getResource("/template1.xlsx").openStream(), sales, doneHandler = {
                it.write(FileOutputStream(File("target/template1.wrote.xlsx")))

                it("mapped cells") {
                    OPCPackage.open(File("target/template1.wrote.xlsx")).use {
                        XSSFWorkbook(it).let {
                            it.getSheet("Sheet1")?.let { sheet ->
                                Assert.assertEquals(sales.subject, sheet.getRow(0).getCell(0).stringCellValue)
                                Assert.assertEquals(sales.date, sheet.getRow(1).getCell(0).dateCellValue)

                                sales.items.forEachIndexed { index, (name, price, number, subtotal) ->
                                    Assert.assertEquals(name, sheet.getRow(3 + index).getCell(0).stringCellValue)
                                    Assert.assertEquals(price.toDouble(), sheet.getRow(3 + index).getCell(1).numericCellValue, 0.0)
                                    Assert.assertEquals(number.toDouble(), sheet.getRow(3 + index).getCell(2).numericCellValue, 0.0)
                                    Assert.assertEquals(subtotal.toDouble(), sheet.getRow(3 + index).getCell(3).numericCellValue, 0.0)
                                }

                                Assert.assertEquals(sales.totalNumber.toDouble(), sheet.getRow(14).getCell(3).numericCellValue, 0.0)
                                Assert.assertEquals(sales.totalFee.toDouble(), sheet.getRow(15).getCell(3).numericCellValue, 0.0)
                            }
                        }
                    }
                }
            })
        }
    }
})
