package com.yo1000.fishbone.acceptor

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author yo1000
 */
object ValueAcceptorSpec : Spek({
    data class Stub(
            val str: String = "ABC",
            val int: Int = 100,
            val date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-05-05 02:15:00")
    )

    given("ValueAcceptor") {
        val acceptor = ValueAcceptor()
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("sheet1")

        sheet.createRow(10).createCell(10).setCellValue("{{ i.str }} [[ i : items : 3,5,V ]]")
        sheet.getRow(10).createCell(11).setCellValue("{{ i.int }}")
        sheet.getRow(10).createCell(12).setCellValue("{{ i.date }}")

        on("accept") {
            acceptor.accept(sheet, mapOf("items" to listOf(Stub(), Stub())), { position, instance ->

                when (position.column) {
                    10 -> {
                        it("visit str value") {
                            Assert.assertEquals(Stub().str, instance)
                        }
                    }
                    11 -> {
                        it("visit int value") {
                            Assert.assertEquals(Stub().int, instance)
                        }
                    }
                    12 -> {
                        it("visit date value") {
                            Assert.assertEquals(Stub().date, instance)
                        }
                    }
                    else -> Unit
                }
            })
        }
    }
})
