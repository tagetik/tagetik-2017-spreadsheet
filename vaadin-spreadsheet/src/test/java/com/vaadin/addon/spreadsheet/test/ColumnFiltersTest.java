package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;

import com.vaadin.addon.spreadsheet.Spreadsheet;

public class ColumnFiltersTest {

    private XSSFWorkbook workbook;
    private Spreadsheet spreadsheet;


    @Before
    public void setUp(){
        workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        //create a column with some data and set it as a sheet filter
        Row row1 = sheet.createRow(1);
        row1.createCell(1).setCellValue("col1");

        Row row2 = sheet.createRow(2);
        row2.createCell(1).setCellValue(1);

        Row row3 = sheet.createRow(3);
        row3.createCell(1).setCellValue(2);

        sheet.setAutoFilter(new CellRangeAddress(1, 3, 1, 1));

        //create another column (to be added)as a table
        Row r1 = sheet.createRow(5);
        r1.createCell(1).setCellValue("col");

        Row r2 = sheet.createRow(6);
        r2.createCell(1).setCellValue(1);

        Row r3 = sheet.createRow(7);
        r3.createCell(1).setCellValue(2);

        spreadsheet = new Spreadsheet(workbook);
    }

    @Test
    public void sheetWithFilters_loadWorkbook_filtersPreserved(){
        assertNotNull(spreadsheet.getTables());
        assertEquals(1, spreadsheet.getTables().size());
        assertEquals(CellRangeAddress.valueOf("B2:B4"),
            spreadsheet.getTables().iterator().next().getFullTableRegion());
    }

    @Test
    public void sheetWithTables_loadWorkbook_tablesPreserved(){
        XSSFTable table = workbook.getSheetAt(0).createTable();
        CTTable ctTable = table.getCTTable();
        ctTable.setRef("B6:B8");

        spreadsheet.setWorkbook(workbook);

        assertNotNull(spreadsheet.getTables());
        assertEquals(2, spreadsheet.getTables().size());
        assertEquals(CellRangeAddress.valueOf("B6:B8"),
            spreadsheet.getTables().iterator().next().getFullTableRegion());
    }
}
