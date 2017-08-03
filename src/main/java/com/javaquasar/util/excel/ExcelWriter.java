package com.javaquasar.util.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Java Quasar
 */
public class ExcelWriter {

    private static final String NAME_SHEET = "Data";
    private ExcelType exelType = null;
    private static Font font;
    private static CellStyle cellstyleFillRed;
    private static CellStyle cellstyleDef;

    public ExcelWriter(ExcelType exelType) {
        this.exelType = exelType;
    }

    /**
     *
     * @param list - data list
     * @param path - path to file
     * @param nameSheet - name sheet in exel file
     * @throws java.io.FileNotFoundException
     */
    public void write(List<List<String>> list, String path, String nameSheet) throws FileNotFoundException, IOException {
        try (Workbook workbook = getWorkbook();
                FileOutputStream fs = new FileOutputStream(path)) {
            Sheet sheet = getSheet(workbook, nameSheet);
            for (int i = 0; i < list.size(); i++) {
                Row row = getRow(i, sheet);
                for (int j = 0; j < list.get(i).size(); j++) {                
                    Cell cell = getCell(row, j);
                    String value = list.get(i).get(j);
                    if (value != null) {
                        value = value.trim();
                        cell.setCellStyle(getCellStyleDef(null, workbook));
                    } else {
                        cell.setCellStyle(getCellStyle(null, workbook));
                    }
                    cell.setCellValue(value);
                    sheet.autoSizeColumn(j);
                }
            }
            workbook.write(fs);
        }
    }

    public void write(List<List<String>> list, String path) throws FileNotFoundException, IOException {
        write(list, path, NAME_SHEET);
    }

    public ExcelType getExelType() {
        return exelType;
    }

    private Workbook getWorkbook() {
        Workbook workbook = null;
        switch (getExelType()) {
            case XLS:
                workbook = new HSSFWorkbook();
                break;
            case XLSX:
                workbook = new XSSFWorkbook();
                break;
        }
        return workbook;
    }

    private Sheet getSheet(Workbook workbook, String name) {

        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(name));
        // Set the width (in units of 1/256th of a character width)
        /*sheet.setColumnWidth(1, 5000);*/

 /*sheet.addMergedRegion(new CellRangeAddress(0, 4, 4, 8));*/
        return sheet;
    }

    private Row getRow(int rowN, /*float height,*/ Sheet sheet) {
        Row row = sheet.createRow(rowN);
        /*row.setHeightInPoints(height);*/

        return row;
    }

    private Cell getCell(Row row, int cellN/*, CellStyle style*/) {
        Cell cell = row.createCell(cellN);
        /*cell.setCellStyle(style);*/
        return cell;
    }

    private CellStyle getCellStyle(Font font, Workbook workbook) {
        if (cellstyleFillRed == null) {
            cellstyleFillRed = workbook.createCellStyle();
            cellstyleFillRed.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cellstyleFillRed.setFillForegroundColor(IndexedColors.RED.getIndex());
            cellstyleFillRed.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
            //cellstyle_fill_red.setAlignment(CellStyle.ALIGN_CENTER);
            cellstyleFillRed.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellstyleFillRed.setWrapText(true);
            //cellstyle_fill_red.setWrapText(true);
            //cellstyle_fill_red.setBorderBottom(CellStyle.BORDER_DASH_DOT_DOT);
            //cellstyle_fill_red.setBottomBorderColor(IndexedColors.GREEN.getIndex());
            if (font != null) {
                cellstyleFillRed.setFont(font);
            }
        }
        return cellstyleFillRed;
    }

    private CellStyle getCellStyleDef(Font font, Workbook workbook) {
        if (cellstyleDef == null) {
            cellstyleDef = workbook.createCellStyle();
            cellstyleDef.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellstyleDef.setWrapText(true);
            if (font != null) {
                cellstyleDef.setFont(font);
            }
        }
        return cellstyleDef;
    }

    private Font getFont(Workbook workbook) {
        if (font == null) {
            font = workbook.createFont();
            font.setFontName("Word");
            font.setFontHeightInPoints((short) 25);
            font.setBold(true);
            font.setStrikeout(true);
            font.setUnderline(Font.U_SINGLE);
            font.setColor(IndexedColors.RED.getIndex());
        }
        return font;
    }

    public static void main(String[] args) throws IOException {
        String[] line1 = {"CLType", "TXN_ID", "CUST_REF", "GPS_DATE", "RULE", "TOKEN", "CardName", "Cr_Type", "TRNX_Type", "TRNX_Code", "PR_Code", "ST_Code", "RC", "RC_Desc", "Pos_DE22", "TXN_AMT", "TXN_CCY", "BILL_AMT", "BILL_CCY", "MERCH_NAME_DE43", "TXN_CTRY", "MERCH_ID_DE42", "POS_TERMNL_DE41", "SETTL_AMT", "SETTLE_CCY", "BLOCK_AMT", "ACT_BAL", "AVL_BAL", "MCC_CODE", "MCC_DESC", "RET_REF_NO_DE37", "AUTH_CODE", "STAN", "ACQID", "POS_DATA_DE61", "ADD_DATA_DE48", "NOTE", "MTID", "TRANID"};
        String[] line2 = {"NORM", "209847564", "1982", "2017-02-06 16:08:02", "DiP-PR-A02^{Usage in Moderate risk countries}; DiP-PR-A03^{Usage Outside SEPA region}", "633730998", "Marie de skowronski", "Plastic", "Authorisation", "Accepted", "01", "00", "00", "All Good", "051", "3,200.00", "764", "-84.93", "978", "BIG ONE PA TING TOWER  PHUKET        THA", "THA", "THANACHART BANK", "T244B065", "84.93", "978", "-86.63", "86.63", "0.00", "6011", "Automated Cash Disburse", "020600005939", "107394", "254453", "011578", "101001000150076483150", "016Z6105000018002TV", "", "0100", "20138"};
        String[] line3 = {"NORM", "209847564", "1982", "2017-02-06 16:08:02", "DiP", "633730998", "Marie de skowronski", "Plastic", "Authorisation", "Accepted", "01", "00", "00", "All Good", "051", "3,200.00", "764", "-84.93", "978", "BIG ONE PA TING TOWER  PHUKET        THA", "THA", "THANACHART BANK", "T244B065", "84.93", "978", "-86.63", "86.63", "0.00", "6011", "Automated Cash Disburse", "020600005939", "107394", "254453", "011578", "101001000150076483150", "016Z6105000018002TV", null, "0100", "20138"};

        
        List<List<String>> list = new ArrayList<>();
        /*for (int i = 0; i < 2; i++) {
            List<String> l = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                l.add("" + i + j + "word" + "abcdfgeklomprqzx");
            }
            list.add(l);
        }*/
        list.add(Arrays.asList(line1));
        list.add(Arrays.asList(line2));
        list.add(Arrays.asList(line3));
        ExcelWriter ew = new ExcelWriter(ExcelType.XLSX);
        ew.write(list, "./word.xlsx");
    }
}
