package BasicPages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DevicePurchasePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Locators ──────────────────────────────────────
    private By inventoryForm    = By.id("inventory-form");
    private By deviceTypeSelect = By.id("deviceType");
    private By brandSelect      = By.id("brand");
    private By devicePreview    = By.id("device-preview");
    private By storage128GB     = By.id("storage-128GB");
    private By unitPriceValue   = By.id("unit-price-value");
    private By colorSelect      = By.id("color");
    private By quantityField    = By.id("quantity");
    private By subtotalValue    = By.id("subtotal-value");
    private By addressField     = By.id("address");
    private By nextBtn          = By.id("inventory-next-btn");
    private By shippingOptions  = By.id("shipping-options");
    private By discountField    = By.id("discount-code");
    private By applyDiscountBtn = By.id("apply-discount-btn");
    private By breakdownTotal   = By.id("breakdown-total-value");
    private By purchaseBtn      = By.id("purchase-device-btn");
    private By successToast     = By.id("purchase-success-toast");
    private By viewHistoryBtn   = By.id("view-history-btn");
    private By invoiceCard      = By.cssSelector("[id^='invoice-item-']");
    private By viewInvoiceBtn   = By.cssSelector("[id^='view-invoice-']");
    private By invoiceTotal     = By.cssSelector("[id^='invoice-total-']");
    private By customerName     = By.cssSelector("[id^='invoice-customer-name-']");

    public DevicePurchasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    public boolean isInventoryFormDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryForm)).isDisplayed();
    }

    public void selectDeviceType(String type) {
        new Select(wait.until(ExpectedConditions.elementToBeClickable(deviceTypeSelect)))
                .selectByVisibleText(type);
    }

    public boolean isBrandDropdownEnabled() {
        return driver.findElement(brandSelect).isEnabled();
    }

    public void selectBrand(String brand) {
        new Select(driver.findElement(brandSelect)).selectByVisibleText(brand);
    }

    public boolean isDevicePreviewDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(devicePreview)).isDisplayed();
    }

    public void selectStorage128GB() {
        WebElement radio = driver.findElement(storage128GB);
        js().executeScript("arguments[0].click();", radio);
    }

    public boolean isStorage128GBSelected() {
        return driver.findElement(storage128GB).isSelected();
    }

    public String getUnitPriceText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(unitPriceValue)).getText();
    }

    public void selectColor(String color) {
        new Select(driver.findElement(colorSelect)).selectByVisibleText(color);
    }

    public String getSelectedColor() {
        return new Select(driver.findElement(colorSelect)).getFirstSelectedOption().getText();
    }

    public void enterQuantity(String qty) {
        WebElement el = driver.findElement(quantityField);
        el.sendKeys(Keys.CONTROL + "a");
        el.sendKeys(Keys.DELETE);
        el.sendKeys(qty);
    }

    public int getQuantityValue() {
        return Integer.parseInt(driver.findElement(quantityField).getAttribute("value"));
    }

    public String getSubtotalText() {
        return driver.findElement(subtotalValue).getText();
    }

    public void enterAddress(String address) {
        WebElement el = driver.findElement(addressField);
        el.clear();
        el.sendKeys(address);
    }

    public String getAddressValue() {
        return driver.findElement(addressField).getAttribute("value");
    }

    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
    }

    public boolean isShippingOptionsDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(shippingOptions)).isDisplayed();
    }

    public void selectShipping(String type) {
        String testId = type.equalsIgnoreCase("express")? "shipping-option-express" : "shipping-option-standard";
        WebElement btn = driver.findElement(By.cssSelector("[data-testid='" + testId + "']"));
        js().executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0,-50);", btn);
        js().executeScript("arguments[0].click();", btn);
    }

    public void selectWarranty(String type) {
        String testId;
        switch (type.toLowerCase()) {
            case "1year": testId = "warranty-1yr";  break;
            case "2year": testId = "warranty-2yr";  break;
            default:      testId = "warranty-none"; break;
        }
        WebElement btn = driver.findElement(By.cssSelector("[data-testid='" + testId + "']"));
        js().executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0,-50);", btn);
        js().executeScript("arguments[0].click();", btn);
    }

    public void applyDiscountCode(String code) throws InterruptedException {
        Thread.sleep(3000);
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(discountField));
        el.clear();
        el.sendKeys(code);
        wait.until(ExpectedConditions.elementToBeClickable(applyDiscountBtn)).click();
    }

    public String getFinalTotalText() {
        return wait.until(driver -> {
            WebElement el = driver.findElement(breakdownTotal);
            String text = el.getText();
            return (text != null && !text.isEmpty()) ? text : null;
        }).replace("R", "").trim();
    }

    public void clickConfirmPurchase() throws InterruptedException {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(purchaseBtn));
        js().executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0,-100);", btn);
        Thread.sleep(1000);
        js().executeScript("arguments[0].click();", btn);
    }

    public WebElement getSuccessToast() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return longWait.until(ExpectedConditions.presenceOfElementLocated(successToast));
    }

    public boolean isToastVisible(WebElement toast) {
        return (Boolean) js().executeScript("var el = arguments[0]; var rect = el.getBoundingClientRect();" + "return rect.width > 0 && rect.height > 0;", toast);
    }

    public void clickViewInvoiceHistory() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(viewHistoryBtn));
        js().executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0,-100);", btn);
        js().executeScript("arguments[0].click();", btn);
    }

    public boolean isInvoiceCardDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(invoiceCard)).isDisplayed();
    }

    public void clickViewInvoice() throws InterruptedException {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(viewInvoiceBtn));
        js().executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0,-100);", btn);
        js().executeScript("arguments[0].click();", btn);
        Thread.sleep(1000);
    }

    public String getInvoiceTotalText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(invoiceTotal)).getText().replace("R", "").trim();
    }

    public String getCustomerNameText() {
        return driver.findElement(customerName).getText();
    }

    private JavascriptExecutor js() {
        return (JavascriptExecutor) driver;
    }

}
