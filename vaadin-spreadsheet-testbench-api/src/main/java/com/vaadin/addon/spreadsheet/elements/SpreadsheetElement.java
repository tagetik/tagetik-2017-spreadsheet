package com.vaadin.addon.spreadsheet.elements;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.AbstractElement;
import com.vaadin.testbench.elementsbase.ServerClass;

/**
 * This is the base element class for accessing a Vaadin Spreadsheet component
 * for TestBench testing.
 * 
 * @author Vaadin Ltd.
 */
@ServerClass("com.vaadin.addon.spreadsheet.Spreadsheet")
public class SpreadsheetElement extends AbstractElement {

    /**
     * Gets the cell element at the given coordinates for the currently active
     * sheet. Throws NoSuchElementException if the cell is outside the visible
     * area.
     * 
     * @param row
     *            Row index, 1-based
     * @param column
     *            Column index, 1-based
     * @return Cell element at the given index.
     * @throws NoSuchElementException
     *             if the cell at (row, column) is not found.
     */
    public SheetCellElement getCellAt(int row, int column) {
        TestBenchElement cell = (TestBenchElement) findElement(By
                .cssSelector(String.format(".col%d.row%d", column, row)));
        return cell.wrap(SheetCellElement.class);
    }

    /**
     * Gets the cell element at the given cell address for the currently active
     * sheet. Throws NoSuchElementException if the cell is outside the visible
     * area.
     * 
     * @param cellAddress
     *            Target address, e.g. A3
     * @return Cell element at the given index.
     * @throws NoSuchElementException
     *             if the cell at (cellAddress) is not found.
     */
    public SheetCellElement getCellAt(String cellAddress) {
        Point point = AddressUtil.addressToPoint(cellAddress);
        return getCellAt(point.getY(), point.getX());
    }

    /**
     * Gets the row header element at the given index.
     * 
     * @param rowIndex
     *            Index of target row, 1-based
     * @return Header of the row at the given index
     */
    public SheetHeaderElement getRowHeader(int rowIndex) {
        TestBenchElement cell = (TestBenchElement) findElement(By
                .cssSelector(String.format(".rh.row%d", rowIndex)));
        return cell.wrap(SheetHeaderElement.class);
    }

    /**
     * Gets the column header element at the given index.
     * 
     * @param columnIndex
     *            Index of target column, 1-based
     * @return Header of the column at the given index
     */
    public SheetHeaderElement getColumnHeader(int columnIndex) {
        TestBenchElement cell = (TestBenchElement) findElement(By
                .cssSelector(String.format(".ch.col%d", columnIndex)));
        return cell.wrap(SheetHeaderElement.class);
    }

    /**
     * Gets the address field. The address field contains the address of the
     * cell that was last clicked, or A1 if no clicks have yet been made.
     * 
     * @return Address field element
     */
    public TestBenchElement getAddressField() {
        return (TestBenchElement) findElement(By.className("addressfield"));
    }

    /**
     * Gets the formula field. This field is where the user can input data for
     * the cell whose address the address field currently contains.
     * 
     * @return Formula field element
     */
    public TestBenchElement getFormulaField() {
        return (TestBenchElement) findElement(By.className("functionfield"));
    }

    /**
     * Gets the info label. Info label is the small text at the bottom right
     * corner of the Spreadsheet.
     * 
     * @return Info label element
     */
    public TestBenchElement getInfoLabel() {
        return (TestBenchElement) findElement(By
                .className("sheet-tabsheet-infolabel"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.testbench.TestBenchElement#scroll(int)
     */
    @Override
    public void scroll(int scrollTop) {
        getBottomRightPane().scroll(scrollTop);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.testbench.TestBenchElement#scrollLeft(int)
     */
    @Override
    public void scrollLeft(int scrollLeft) {
        getBottomRightPane().scrollLeft(scrollLeft);
    }

    /**
     * Scrolls the sheet selector to the beginning.
     * 
     * Has no effect if there are not enough sheets to require scrolling.
     */
    public void scrollSheetsToStart() {
        findElement(By.className("scroll-tabs-beginning")).click();
    }

    /**
     * Scrolls the sheet selector to the end.
     * 
     * Has no effect if there are not enough sheets to require scrolling.
     */
    public void scrollSheetsToEnd() {
        findElement(By.className("scroll-tabs-end")).click();
    }

    /**
     * Scrolls the sheet selector left or right by the given amount.
     * 
     * Has no effect if there are not enough sheets to require scrolling.
     * 
     * @param amount
     *            Amount to scroll. Positive numbers scroll to the right and
     *            negative numbers scroll to the left.
     */
    public void scrollSheets(int amount) {
        WebElement target = findElement(By
                .className(amount > 0 ? "scroll-tabs-right"
                        : "scroll-tabs-left"));
        for (int i = 0; i < amount; i++) {
            target.click();
        }
    }

    /**
     * Selects the sheet at the given index. Indexes are counted only for
     * visible sheets.
     * 
     * @param sheetIndex
     *            Index of sheet to select, 0-based
     */
    public void selectSheetAt(int sheetIndex) {
        WebElement tabContainer = findElement(By
                .className("sheet-tabsheet-container"));
        List<WebElement> tabs = tabContainer.findElements(By.xpath(".//*"));
        WebElement target = tabs.get(sheetIndex);
        scrollSheetVisible(target);
        target.click();
    }

    /**
     * Selects the sheet with the given name. Only visible sheets can be
     * selected.
     * 
     * @param sheetName
     *            Name of sheet to select
     */
    public void selectSheet(String sheetName) {
        WebElement tabContainer = findElement(By
                .className("sheet-tabsheet-container"));
        List<WebElement> tabs = tabContainer.findElements(By.xpath(".//*"));
        for (WebElement tab : tabs) {
            if (tab.getText().equals(sheetName)) {
                scrollSheetVisible(tab);
                tab.click();
                break;
            }
        }
    }

    /**
     * Adds a new sheet with the given name. Sheet names should be unique within
     * a spreadsheet.
     * 
     * @param sheetName
     *            Name of the new sheet.
     */
    public void addSheet(String sheetName) {
        findElement(By.className("add-new-tab")).click();
    }

    private void scrollSheetVisible(WebElement targetSheet) {
        // Make sure the target sheet is visible
        if (!targetSheet.isDisplayed()) {
            scrollSheetsToStart();
            while (!targetSheet.isDisplayed()) {
                scrollSheets(1);
            }
        }
    }

    // Current selection
    private WebElement sTop;
    private WebElement sBottom;
    private WebElement sRight;
    private Point sLocation;
    private Dimension sSize;

    boolean isElementSelected(WebElement element) {
        updateSelectionLocationAndSize();
        return intersectsSelection(element.getLocation(), element.getSize())
                || isNonCoherentlySelected(element);
    }

    private void findSelectionOutline() {
        // sometimes the spreadsheet takes so long to load that the selection
        // widget elements are not found
        try {
            sTop = findElement(By.className("s-top"));
        } catch (NoSuchElementException nsee) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            sTop = findElement(By.className("s-top"));
        }
        sBottom = findElement(By.className("s-bottom"));
        // Just to make sure the left element is present
        findElement(By.className("s-left"));
        sRight = findElement(By.className("s-right"));
    }

    private boolean isNonCoherentlySelected(WebElement element) {
        // an element is non-coherently selected if the background color is
        // rgba(224, 245, 255, 0.8) or if it has a solid outline style
        return "rgba(224, 245, 255, 0.8)".equals(element
                .getCssValue("background-color"))
                || "solid".equals(element.getCssValue("outline-style"));
    }

    private void updateSelectionLocationAndSize() {
        if (sTop == null) {
            findSelectionOutline();
        }
        sLocation = sTop.getLocation();
        int bottomY = sBottom.getLocation().getY();
        int bottomH = sBottom.getSize().getHeight();
        int rightX = sRight.getLocation().getX();
        int rightW = sRight.getSize().getWidth();
        sSize = new Dimension(rightX + rightW - sLocation.getX(), bottomY
                + bottomH - sLocation.getY());
    }

    private boolean intersectsSelection(Point location, Dimension size) {
        // Test top left corner
        if (location.getX() < sLocation.getX()
                || location.getY() < sLocation.getY()) {
            return false;
        }
        // Test lower right corner
        if (location.getX() + size.getWidth() > sLocation.getX()
                + sSize.getWidth()
                || location.getY() + size.getHeight() > sLocation.getY()
                        + sSize.getHeight()) {
            return false;
        }
        // Everything is inside the selection
        return true;
    }

    WebElement getCellValueInput() {
        return findElement(By.className("cellinput"));
    }

    private TestBenchElement getBottomRightPane() {
        return wrapElement(findElement(By.className("bottom-right-pane")),
                getCommandExecutor());
    }
}