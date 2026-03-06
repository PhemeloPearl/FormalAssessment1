package Utilities;

import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class ReadXSLdata {

    @DataProvider(name = "testData")
    public static String[][] getData(Method method) throws IOException {
        String excelSheetName = method.getName();
        File file = new File(System.getProperty("user.dir")
                + "/src/test/resources/testdata/testdata.xlsx");
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheetName = workbook.getSheet(excelSheetName);

        int totalRows  = sheetName.getLastRowNum();
        Row rowCells   = sheetName.getRow(0);
        int totalCols  = rowCells.getLastCellNum();

        DataFormatter formatter = new DataFormatter();
        String[][] testData     = new String[totalRows][totalCols];

        for (int i = 1; i <= totalRows; i++) {
            Row row = sheetName.getRow(i);
            for (int j = 0; j < totalCols; j++) {
                testData[i - 1][j] = formatter.formatCellValue(row.getCell(j));
            }
        }
        workbook.close();
        return testData;
    }
}