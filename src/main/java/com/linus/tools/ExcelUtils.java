package com.linus.tools;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

public class ExcelUtils {

    private static void displayIndex(XSSFWorkbook xssfWorkbook) {
        int sheetNumber = xssfWorkbook.getNumberOfSheets ();
        for (int i = 0; i < sheetNumber; i++) {
            XSSFSheet sheet = xssfWorkbook.getSheetAt (i);
            System.out.println (sheet.getSheetName ());
            System.out.println (sheet.getFirstRowNum ());
            System.out.println (sheet.getLastRowNum ());
            String sheetName = sheet.getSheetName ();
            if (!sheetName.equals ("Policy Description")) {
                logger.info ("ignore sheet " + sheetName);
                continue;
            }
            int firstRowNumber = sheet.getFirstRowNum ();
            int lastRowNumber = sheet.getLastRowNum ();
            for (int rowIndex = firstRowNumber; rowIndex < lastRowNumber; rowIndex++) {
                Row row = sheet.getRow (rowIndex);
                if (row == null) {
                    // This whole row is empty
                    // Handle it as needed
                    continue;
                }
                int lastColumn = row.getLastCellNum ();
                for (int cellIndex = 0; cellIndex < lastColumn; cellIndex++) {
                    Cell cell = row.getCell (cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null) {
                        // The spreadsheet is empty in this cell
                        logger.info ("Empty cell in row " + rowIndex + ", cell " + cellIndex);
                    } else {
                        logger.warn (cell.getColumnIndex ());
                    }
                }
            }
        }
    }

    private static void iterateCellContent(XSSFWorkbook xssfWorkbook) {
        int sheetNumber = xssfWorkbook.getNumberOfSheets ();
        for (int i = 0; i < sheetNumber; i++) {
            XSSFSheet sheet = xssfWorkbook.getSheetAt (i);
            String sheetName = sheet.getSheetName ();
            if (!sheetName.equals ("Policy Description")) {
                logger.info ("ignore sheet " + sheetName);
                continue;
            }
            for (Row row : sheet) {
                if (row.getLastCellNum () < 8) {
                    logger.error("cell number is to small");
                    continue;
                }
                String lacci = row.getCell (0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue ().trim ();
                String provinceName = row.getCell (4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue ().trim ();
                String cityName = row.getCell (5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue ().trim ();
                String remark = row.getCell (7, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue ().trim ();
                logger.info (String.format ("lacci: %s, province name: %s, city name: %s, remark: %s", lacci, provinceName, cityName, remark));
            }
        }
    }

    private static final Logger logger = Logger.getLogger (ExcelUtils.class);
    public static void main (String[] args) {
        PropertyConfigurator.configure ("conf/log4j.properties");
        try {
            File file = new File ("data/ieglo_lacima_ref_20180114-System.xlsx");
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook (file);
//            List<XSSFName> names = xssfWorkbook.getAllNames ();
//            for (XSSFName name : names) {
//                System.out.println (name.getSheetName ());
//            }
//            displayIndex (xssfWorkbook);
            iterateCellContent (xssfWorkbook);
        } catch (IOException ioe) {
            ioe.printStackTrace ();
        } catch (InvalidFormatException ife) {
            ife.printStackTrace ();
        }
    }
}
