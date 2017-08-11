package com.javaquasar.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Java Quasar
 */
public class ExcelReader {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

    public static List<List<String>> readOnlyStrings(String path) throws IOException {
        Workbook wb = getWorkbook(path);
        List<List<String>> list = new ArrayList<>();
        for (Row r : wb.getSheetAt(0)) {
            List<String> l = new ArrayList<>();
            for (Cell c : r) {
                l.add(c.getStringCellValue());
            }
            list.add(l);
        }
        return list;
    }

    public List<List<Object>> readAllType(String path) throws IOException {
        Workbook wb = getWorkbook(path);
        List<List<Object>> list = new ArrayList<>();
        for (Row r : wb.getSheetAt(0)) {
            List<Object> l = new ArrayList<>();
            for (Cell c : r) {
                l.add(getCellText(c));
            }
            list.add(l);
        }
        return list;
    }

    public static Workbook getWorkbook(String path, ExcelType exelType) throws FileNotFoundException, IOException {
        Workbook workbook = null;
        switch (exelType) {
            case XLS:
                workbook = new HSSFWorkbook(new FileInputStream(path));
                break;
            case XLSX:
                workbook = new XSSFWorkbook(new FileInputStream(path));
                break;
        }
        return workbook;
    }

    public static Workbook getWorkbook(String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        Workbook workbook = null;
        try {
            // xls
            workbook = new XSSFWorkbook(new FileInputStream(file));
        } catch (Exception exception) {
            // xlsx
            try {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } catch (Exception e) {
                throw e;
            }
        }
        return workbook;
    }

    public static String getCellText(Cell cell) {
        String result = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        result = sdf.format(cell.getDateCellValue());
                    } else {
                        result = Double.toString(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula().toString();
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        ExcelReader.readOnlyStrings("./word.xlsx");
    }
}
