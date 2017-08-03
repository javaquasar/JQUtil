package com.javaquasar.util.excel;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Java Quasar
 */
public class ExcelManager {
    
    public static void write(List<List<String>> dataList, String pathToFile) throws IOException {
        ExcelWriter ew = new ExcelWriter(ExcelType.XLSX);
        ew.write(dataList, pathToFile);
    }
}
    
