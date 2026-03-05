/*import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class loginToNdosiWebsite {

    WebDriver driver;
    WebDriverWait wait;

    double unitPrice;
    int quantity;
    double expectedSubtotal;

    @Test
    public void loginWithCredentials() throws InterruptedException {

        driver = new ChromeDriver();
        driver.get("https://ndosisimplifiedautomation.vercel.app/");
        driver.manage().window().maximize();

        // Click the login window
        driver.findElement(By.xpath("//*[@id=\"app-root\"]/nav/div[1]/div[3]/button/span[2]")).click();

        driver.findElement(By.id("login-email")).sendKeys("sandra@gmail.com");
        driver.findElement(By.id("login-password")).sendKeys("sanDRA21#");
        driver.findElement(By.id("login-submit")).click();

        Thread.sleep(3000);

        String myCoursesText = driver.findElement(By.xpath("//*[@id=\"app-main-content\"]/section/div[1]/h2")).getText();

        Assert.assertEquals(myCoursesText, "Welcome back, Montjo! 👋");

        Thread.sleep(2000);

        driver.findElement(By.xpath("//*[@id=\"app-root\"]/nav/div[1]/div[2]/div[1]/button/span[2]")).click();

        driver.findElement(By.cssSelector("#app-root > nav > div.nav-container > div.nav-items > div:nth-child(4) > div > button:nth-child(2)")).click();

        Thread.sleep(2000);

        String practiceHeadingText = driver.findElement(By.id("practice-heading")).getText();
        Assert.assertEquals(practiceHeadingText, "Welcome back, Montjo!");

        driver.findElement(By.id("tab-btn-web")).click();

        // Select Device
        Select deviceDropDown = new Select(driver.findElement(By.id("deviceType")));
        deviceDropDown.selectByVisibleText("Phone");

        // Select Brand
        Select brandDropDown = new Select(driver.findElement(By.id("brand")));
        brandDropDown.selectByVisibleText("Apple");

        // Select Storage
        WebElement phoneRadio = driver.findElement(By.id("storage-128GB"));
        phoneRadio.click();
        Assert.assertTrue(phoneRadio.isSelected());

        // Get Unit Price
        WebElement unitPriceElement = driver.findElement(By.id("unit-price-value"));
        String unitPriceText = unitPriceElement.getText();
        Assert.assertEquals(unitPriceText, "R480.00");

        unitPrice = Double.parseDouble(unitPriceText.replace("R", ""));

        // Select Color
        Select colorDropDown = new Select(driver.findElement(By.id("color")));
        colorDropDown.selectByVisibleText("Blue");

        // Quantity
        WebElement quantityNumber = driver.findElement(By.id("quantity"));
        quantityNumber.sendKeys(Keys.CONTROL + "a");
        quantityNumber.sendKeys(Keys.DELETE);
        quantityNumber.sendKeys("2");

        quantity = Integer.parseInt(quantityNumber.getAttribute("value"));
        Assert.assertEquals(quantity, 2);

        // Subtotal Validation
        double expectedSubtotal = unitPrice * quantity;

        WebElement subtotalElement = driver.findElement(By.id("subtotal-value"));
        double actualSubtotal = Double.parseDouble(subtotalElement.getText().replace("R", ""));

        Assert.assertEquals(actualSubtotal, expectedSubtotal);

        // Address
        WebElement addressField = driver.findElement(By.id("address"));
        addressField.clear();
        addressField.sendKeys("123 Test Street");
        Assert.assertEquals(addressField.getAttribute("value"), "123 Test Street");

        // Click Next
        driver.findElement(By.id("inventory-next-btn")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("shipping-options")));

        // ✅ 10️⃣ Select Express Shipping
        selectShipping("express");

        // ✅ 11️⃣ Select 1 Year Warranty
        selectWarranty("1year");

        Thread.sleep(3000);


        expectedSubtotal = unitPrice * quantity;;

        // Apply Discount Code
        WebElement discountField = driver.findElement(By.id("discount-code"));
        discountField.clear();
        discountField.sendKeys("SAVE10");

        driver.findElement(By.id("apply-discount-btn")).click();

        // Calculate Expected Final Total
        double shippingCost = 25.00;
        double warrantyCost = 49.00;

        double totalBeforeDiscount = expectedSubtotal + shippingCost + warrantyCost;

        double discountAmount = totalBeforeDiscount * 0.10;

        double expectedFinalTotal = totalBeforeDiscount - discountAmount;

        // Round to 2 decimal places
        expectedFinalTotal = Math.round(expectedFinalTotal * 100.0) / 100.0;


        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until the element has visible text
        String finalTotalText = wait.until(driver -> {
            WebElement el = driver.findElement(By.id("breakdown-total-value"));
            String text = el.getText();
            return (text != null && !text.isEmpty()) ? text : null;
        }).replace("R", "").trim();

        System.out.println("Final Total: " + finalTotalText); // Should print 930.60

        double actualFinalTotal = Double.parseDouble(finalTotalText);

        // Assert Final Total
        Assert.assertEquals(actualFinalTotal, expectedFinalTotal);


        driver.findElement(By.id("purchase-device-btn")).click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click "View Invoice History"
        WebElement viewHistoryBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("view-history-btn")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewHistoryBtn);
        viewHistoryBtn.click();

// Wait until at least one invoice card is visible (use contains 'invoice-item-' in id)
        WebElement invoiceCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[id^='invoice-item-']")));

// Then click the "View" button inside that invoice card
        WebElement viewInvoiceBtn = invoiceCard.findElement(By.cssSelector("[id^='view-invoice-']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewInvoiceBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewInvoiceBtn);

        // Wait for the element to be visible
         wait = new WebDriverWait(driver, Duration.ofSeconds(20));
       WebElement orderSuccessMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(normalize-space(.),'Order Successful')]")
        ));

// Scroll into view just in case
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orderSuccessMsg);

// Assert the text contains "Order Successful"
        Assert.assertTrue(orderSuccessMsg.getText().contains("Order Successful"));
        System.out.println("Order Success message detected!");

// Assert the invoice total matches final total
        WebElement invoiceTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[id^='invoice-total-']")));
        Assert.assertEquals(invoiceTotal.getText().replace("R", "").trim(), finalTotalText);
    }

    public void selectShipping(String type) {
        if (type.equalsIgnoreCase("express")) {
            driver.findElement(By.cssSelector("[data-testid='shipping-option-express']")).click();
        } else if (type.equalsIgnoreCase("standard")) {
            driver.findElement(By.cssSelector("[data-testid='shipping-option-standard']")).click();
        }
    }

    // 🔹 Warranty Method
    public void selectWarranty(String type) {

        if (type.equalsIgnoreCase("none")) {

            WebElement none = driver.findElement(By.cssSelector("[data-testid='warranty-none']"));
            none.click();
            Assert.assertTrue(none.isSelected());

        } else if (type.equalsIgnoreCase("1year")) {

            WebElement oneYear = driver.findElement(By.cssSelector("[data-testid='warranty-1yr']"));
            oneYear.click();
            Assert.assertTrue(oneYear.isSelected());
        } else if (type.equalsIgnoreCase("2year")) {

            WebElement twoYear = driver.findElement(By.cssSelector("[data-testid='warranty-2yr']"));
            twoYear.click();
            Assert.assertTrue(twoYear.isSelected());
        }


    }
}*/





import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class loginToNdosiWebsite {

    WebDriver driver;
    WebDriverWait wait;

    double unitPrice;
    int quantity;
    double expectedSubtotal;

    @Test
    public void loginWithCredentials() throws InterruptedException {

        driver = new ChromeDriver();
        driver.get("https://ndosisimplifiedautomation.vercel.app/");
        driver.manage().window().maximize();

        // Click the login window
        driver.findElement(By.xpath("//*[@id=\"app-root\"]/nav/div[1]/div[3]/button/span[2]")).click();

        driver.findElement(By.id("login-email")).sendKeys("sandra@gmail.com");
        driver.findElement(By.id("login-password")).sendKeys("sanDRA21#");
        driver.findElement(By.id("login-submit")).click();

        Thread.sleep(3000);

        String myCoursesText = driver.findElement(By.xpath("//*[@id=\"app-main-content\"]/section/div[1]/h2")).getText();
        Assert.assertEquals(myCoursesText, "Welcome back, Montjo! 👋");

        Thread.sleep(2000);

        driver.findElement(By.xpath("//*[@id=\"app-root\"]/nav/div[1]/div[2]/div[1]/button/span[2]")).click();
        driver.findElement(By.cssSelector("#app-root > nav > div.nav-container > div.nav-items > div:nth-child(4) > div > button:nth-child(2)")).click();

        Thread.sleep(2000);

        String practiceHeadingText = driver.findElement(By.id("practice-heading")).getText();
        Assert.assertEquals(practiceHeadingText, "Welcome back, Montjo!");

        driver.findElement(By.id("tab-btn-web")).click();

        // Select Device
        Select deviceDropDown = new Select(driver.findElement(By.id("deviceType")));
        deviceDropDown.selectByVisibleText("Phone");

        // Select Brand
        Select brandDropDown = new Select(driver.findElement(By.id("brand")));
        brandDropDown.selectByVisibleText("Apple");

        // Select Storage
        WebElement phoneRadio = driver.findElement(By.id("storage-128GB"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", phoneRadio);
        Assert.assertTrue(phoneRadio.isSelected());

        // Get Unit Price
        WebElement unitPriceElement = driver.findElement(By.id("unit-price-value"));
        String unitPriceText = unitPriceElement.getText();
        Assert.assertEquals(unitPriceText, "R480.00");
        unitPrice = Double.parseDouble(unitPriceText.replace("R", ""));

        // Select Color
        Select colorDropDown = new Select(driver.findElement(By.id("color")));
        colorDropDown.selectByVisibleText("Blue");

        // Quantity
        WebElement quantityNumber = driver.findElement(By.id("quantity"));
        quantityNumber.sendKeys(Keys.CONTROL + "a");
        quantityNumber.sendKeys(Keys.DELETE);
        quantityNumber.sendKeys("2");
        quantity = Integer.parseInt(quantityNumber.getAttribute("value"));
        Assert.assertEquals(quantity, 2);

        // Subtotal Validation
        expectedSubtotal = unitPrice * quantity;
        WebElement subtotalElement = driver.findElement(By.id("subtotal-value"));
        double actualSubtotal = Double.parseDouble(subtotalElement.getText().replace("R", ""));
        Assert.assertEquals(actualSubtotal, expectedSubtotal);

        // Address
        WebElement addressField = driver.findElement(By.id("address"));
        addressField.clear();
        addressField.sendKeys("123 Test Street");
        Assert.assertEquals(addressField.getAttribute("value"), "123 Test Street");

        // Click Next
        driver.findElement(By.id("inventory-next-btn")).click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("shipping-options")));

        // ✅ Select Express Shipping
        selectShipping("express");

        // ✅ Select 1 Year Warranty
        selectWarranty("1year");

        Thread.sleep(3000);

        // Apply Discount Code
        WebElement discountField = driver.findElement(By.id("discount-code"));
        discountField.clear();
        discountField.sendKeys("SAVE10");
        driver.findElement(By.id("apply-discount-btn")).click();

        // Calculate Expected Final Total
        double shippingCost = 25.00;
        double warrantyCost = 49.00;
        double totalBeforeDiscount = expectedSubtotal + shippingCost + warrantyCost;
        double discountAmount = totalBeforeDiscount * 0.10;
        double expectedFinalTotal = Math.round((totalBeforeDiscount - discountAmount) * 100.0) / 100.0;

        // Wait until final total is visible
        String finalTotalText = wait.until(driver -> {
            WebElement el = driver.findElement(By.id("breakdown-total-value"));
            String text = el.getText();
            return (text != null && !text.isEmpty()) ? text : null;
        }).replace("R", "").trim();
        double actualFinalTotal = Double.parseDouble(finalTotalText);
        Assert.assertEquals(actualFinalTotal, expectedFinalTotal);

        // Purchase
        driver.findElement(By.id("purchase-device-btn")).click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ✅ Click "View Invoice History"
        WebElement viewHistoryBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("view-history-btn")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0,-100);", viewHistoryBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewHistoryBtn);

        // Wait until at least one invoice card is visible
        WebElement invoiceCard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id^='invoice-item-']")));

        // Click the "View" button inside the invoice card
        WebElement viewInvoiceBtn = invoiceCard.findElement(By.cssSelector("[id^='view-invoice-']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0,-100);", viewInvoiceBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewInvoiceBtn);

        // Wait for Order Successful message
        WebElement orderSuccessMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[contains(normalize-space(.),'Order Successful')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orderSuccessMsg);
        Assert.assertTrue(orderSuccessMsg.getText().contains("Order Successful"));
        System.out.println("Order Success message detected!");

        // Assert invoice total matches final total
        WebElement invoiceTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id^='invoice-total-']")));
        Assert.assertEquals(invoiceTotal.getText().replace("R", "").trim(), finalTotalText);
    }

    // 🔹 Shipping Method
    public void selectShipping(String type) {
        WebElement shippingBtn;
        if (type.equalsIgnoreCase("express")) {
            shippingBtn = driver.findElement(By.cssSelector("[data-testid='shipping-option-express']"));
        } else {
            shippingBtn = driver.findElement(By.cssSelector("[data-testid='shipping-option-standard']"));
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0,-50);", shippingBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", shippingBtn);
    }

    // 🔹 Warranty Method
    public void selectWarranty(String type) {
        WebElement warrantyBtn = null;
        if (type.equalsIgnoreCase("none")) {
            warrantyBtn = driver.findElement(By.cssSelector("[data-testid='warranty-none']"));
        } else if (type.equalsIgnoreCase("1year")) {
            warrantyBtn = driver.findElement(By.cssSelector("[data-testid='warranty-1yr']"));
        } else if (type.equalsIgnoreCase("2year")) {
            warrantyBtn = driver.findElement(By.cssSelector("[data-testid='warranty-2yr']"));
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0,-50);", warrantyBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", warrantyBtn);
        Assert.assertTrue(warrantyBtn.isSelected());
    }
}