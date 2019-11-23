/*
 * This Example show the usage the .deps and the capacity to work with excel workbook
 *
 * The .deps file should contains this dependencies :
 * org.apache.poi:poi:4.1.1
 * org.apache.poi:poi-ooxml:4.1.1
 */

package examples

import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.*

def workbook = new XSSFWorkbook()
 
def sheet = workbook.createSheet("Persons")
sheet.setColumnWidth(0, 6000)
sheet.setColumnWidth(1, 4000)
 
def header = sheet.createRow(0)
 
def headerStyle = workbook.createCellStyle()
headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex())
headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
 
def font = ((XSSFWorkbook) workbook).createFont()
font.setFontName("Arial")
font.setFontHeightInPoints((short) 16)
font.setBold(true)
headerStyle.setFont(font)
 
def headerCell = header.createCell(0)
headerCell.setCellValue("Name")
headerCell.setCellStyle(headerStyle)
 
headerCell = header.createCell(1)
headerCell.setCellValue("Age")
headerCell.setCellStyle(headerStyle)

def xlsx = promptSaveFile(options = [ title: "Excel Script", filterExtensions: ["*.xlsx"], filename: null ])

workbook.write(new FileOutputStream(xlsx))
workbook.close()