package com.hitec.presentation.util

import android.util.Log
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ExcelHelper {
    private const val TAG = "ExcelHelper"

    fun savePeriodData(
        dataList: List<String>,
        consumerHouseNumber: String,
    ) {
        val dir = PathHelper.isExistDir("SmartSetRenew/period")
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd_HHmmssSS"))
        val fileName = "${consumerHouseNumber}_$currentDateTime.xlsx"
        val filePath = File(dir, fileName)

        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Period Data")

            val headerRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("Time")
            headerRow.createCell(1).setCellValue("Value")

            dataList.forEachIndexed { index, data ->
                val row = sheet.createRow(index + 1)
                val splitData = data.split(": ")
                row.createCell(0).setCellValue(splitData[0])
                row.createCell(1).setCellValue(splitData[1])
            }

            FileOutputStream(filePath).use { outputStream ->
                workbook.write(outputStream)
            }
            workbook.close()
            Log.i(TAG, "Excel file saved at: $filePath")
        } catch (e: IOException) {
            Log.e(TAG, "Error while saving Excel file: ${e.message}")
        }
    }
}