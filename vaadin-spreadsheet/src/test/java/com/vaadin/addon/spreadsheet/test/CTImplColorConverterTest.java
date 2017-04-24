package com.vaadin.addon.spreadsheet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.XSSFColorConverter;
import com.vaadin.addon.spreadsheet.test.pageobjects.SpreadsheetPage;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;

/**
 * Created by chiarama on 4/24/2017.
 */
@RunLocally(Browser.PHANTOMJS)
public class CTImplColorConverterTest extends AbstractSpreadsheetTestCase {

    private static final String BACKGROUND_COLOR = "background-color";
    private XSSFWorkbook workbook;
    private XSSFColorConverter colorConverter;
    private SpreadsheetPage spreadsheetPage;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        InputStream is = getClass()
            .getResourceAsStream("/test_sheets/wrong_ctColor.xlsx");

        workbook = new XSSFWorkbook(is);
        spreadsheetPage = headerPage.loadFile("wrong_ctColor.xlsx", this);
        colorConverter = new XSSFColorConverter(workbook);
    }

    @Test
    public void customIndexedColor_compareBackgroundColor_consistentColors() throws
        IOException {

        XSSFSheetConditionalFormatting conditionalFormatting = workbook.getSheetAt(0).getSheetConditionalFormatting();
        XSSFConditionalFormattingRule rule = conditionalFormatting.getConditionalFormattingAt(0).getRule(0);
        String backgroundColorCSS = colorConverter.getBackgroundColorCSS(rule);

        assertNotNull(backgroundColorCSS);

        String cssBackgroundColorValue = spreadsheetPage.getCellAt(1, 1)
            .getCssValue(BACKGROUND_COLOR);

        // ignore some parts of the css to avoid failures such as
        // "Expected :rgba(232, 232, 232, 1.0);  Actual   :rgba(232, 232, 232, 1)"
        assertEquals(backgroundColorCSS.substring(0,21), cssBackgroundColorValue.substring(0,21));
    }
}
