package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

@RunLocally(Browser.PHANTOMJS)
public class ConditionalFormattingCellValueIsTest extends AbstractSpreadsheetTestCase {

    private static final String STRING_VALUE = "'Foo";
    private static final String NUMBER_VALUE = "1";
    private static final String DIFFERENT_NUMBER_VALUE = "2";
    private static final String TRUE_VALUE = "TRUE";
    private static final String FALSE_VALUE = "FALSE";
    private static final String FALSE_CONDITION_COLOR = "rgba(255, 255, 255, 1)";
    private static final String TRUE_CONDITION_COLOR = "rgba(255, 0, 0, 1)";
    
    private SpreadsheetPage spreadsheetPage;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        spreadsheetPage = headerPage
            .loadFile("conditional_formatting_cell_is.xlsx", this);
        spreadsheetPage.selectSheetAt(1);
    }

    @Test
    public void loadSpreadsheetWithEqualConditionFormattingInB3_MakeConditionFalse_CellB3FilledWhite() {
        spreadsheetPage.setCellValue("B2", STRING_VALUE);
        spreadsheetPage.setCellValue("B3", "Not"+STRING_VALUE);

        spreadsheetPage.setCellValue("D2", NUMBER_VALUE);
        spreadsheetPage.setCellValue("D3", DIFFERENT_NUMBER_VALUE);

        spreadsheetPage.setCellValue("F2", TRUE_VALUE);
        spreadsheetPage.setCellValue("F3", FALSE_VALUE);

        String cellColorStringCase = spreadsheetPage.getCellColor("B3");
        String cellColorNumberCase = spreadsheetPage.getCellColor("D3");
        String cellColorBooleanCase = spreadsheetPage.getCellColor("F3");

        assertEquals(FALSE_CONDITION_COLOR, cellColorStringCase);
        assertEquals(FALSE_CONDITION_COLOR, cellColorNumberCase);
        assertEquals(FALSE_CONDITION_COLOR, cellColorBooleanCase);
    }

    @Test
    public void loadSpreadsheetWithEqualConditionFormattingInB3_MakeConditionTrue_CellB3FilledRed() {
        spreadsheetPage.setCellValue("B2", STRING_VALUE);
        spreadsheetPage.setCellValue("B3", STRING_VALUE);

        spreadsheetPage.setCellValue("D2", NUMBER_VALUE);
        spreadsheetPage.setCellValue("D3", NUMBER_VALUE);

        spreadsheetPage.setCellValue("F2", TRUE_VALUE);
        spreadsheetPage.setCellValue("F3", TRUE_VALUE);


        String cellColorStringCase = spreadsheetPage.getCellColor("B3");
        String cellColorNumberCase = spreadsheetPage.getCellColor("D3");
        String cellColorBooleanCase = spreadsheetPage.getCellColor("F3");

        assertEquals(TRUE_CONDITION_COLOR, cellColorStringCase);
        assertEquals(TRUE_CONDITION_COLOR, cellColorNumberCase);
        assertEquals(TRUE_CONDITION_COLOR, cellColorBooleanCase);
    }

    @Test
    public void loadSpreadsheetWithNotEqualConditionFormattingInB4_insertIncoherentValue_CellB4FilledRed() {
        spreadsheetPage.setCellValue("B2", STRING_VALUE);
        spreadsheetPage.setCellValue("B4", NUMBER_VALUE);
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B4"));
    }

    @Test
    public void loadSpreadsheetWithNotEqualToNumberZeroConditionFormattingRuleInBlankCell_insertValue_CellB5FilledRed() {
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B5"));
        spreadsheetPage.setCellValue("B5", NUMBER_VALUE);
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B5"));
    }

    @Test
    public void loadSpreadsheetWithEqualToNumberZeroConditionFormattingRuleInBlankCell_insertValue_CellB6FilledWhite() {
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B6"));
        spreadsheetPage.setCellValue("B6", NUMBER_VALUE);
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B6"));
    }

    @Test
    public void loadSpreadsheetWithNotEqualToBooleanFalseConditionFormattingRuleInBlankCell_insertValue_CellB7FilledRed() {
        assertEquals(FALSE_CONDITION_COLOR, spreadsheetPage.getCellColor("B7"));
        spreadsheetPage.setCellValue("B7", FALSE_VALUE);
        assertEquals(TRUE_CONDITION_COLOR, spreadsheetPage.getCellColor("B7"));
    }
}
