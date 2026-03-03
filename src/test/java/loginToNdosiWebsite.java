import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

public class loginToNdosiWebsite {

    WebDriver driver;

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

        Thread.sleep(5000);

        driver.findElement(By.xpath("//*[@id=\"app-main-content\"]/section/div[1]/h2")).isDisplayed();

        String myCoursesText = driver.findElement(By.xpath("//*[@id=\"app-main-content\"]/section/div[1]/h2")).getText();

        System.out.println(myCoursesText);

        Assert.assertEquals(myCoursesText, "Welcome back, Montjo! 👋");

        Thread.sleep(5000);

        driver.findElement(By.xpath("//*[@id=\"app-root\"]/nav/div[1]/div[2]/div[1]/button/span[2]")).click();
        driver.findElement(By.cssSelector("#app-root > nav > div.nav-container > div.nav-items > div:nth-child(4) > div > button:nth-child(2)")).click();

        Thread.sleep(5000);

        driver.findElement(By.id("practice-heading")).isDisplayed();

        String practiceHeadingText = driver.findElement(By.id("practice-heading")).getText();
        System.out.println(practiceHeadingText);
        Assert.assertEquals(practiceHeadingText, "Welcome back, Montjo!");

        Thread.sleep(5000);

        driver.findElement(By.id("tab-btn-web")).click();

        Select deviceDropDown = new Select(driver.findElement(By.id("deviceType")));
        deviceDropDown.selectByVisibleText("Phone");

        Select brandDropDown = new Select(driver.findElement(By.id("brand")));
        brandDropDown.selectByVisibleText("Apple");

        WebElement phoneRadio = driver.findElement(By.id("storage-128GB"));
        phoneRadio.click();
        Assert.assertTrue(phoneRadio.isSelected());

        WebElement unitPriceElement = driver.findElement(By.id("unit-price-value"));
        String unitPriceText = unitPriceElement.getText();
        Assert.assertEquals(unitPriceText, "R480.00");

        double unitPrice = Double.parseDouble(unitPriceText.replace("R", ""));
        Thread.sleep(3000);


        Select colorDropDown = new Select(driver.findElement(By.id("color")));
        colorDropDown.selectByVisibleText("Blue");
        Assert.assertTrue(colorDropDown.getFirstSelectedOption().getText().equals("Blue"));

        WebElement quantityNumber = driver.findElement(By.id("quantity"));

        quantityNumber.sendKeys(Keys.CONTROL + "a");
        quantityNumber.sendKeys(Keys.DELETE);
        quantityNumber.sendKeys("2");

        int quantity = Integer.parseInt(quantityNumber.getAttribute("value"));
        Assert.assertEquals(quantity, 2);

        double expectedSubtotal = unitPrice * quantity;

        WebElement subtotalElement = driver.findElement(By.id("subtotal-value"));
        String subtotalText = subtotalElement.getText();
        double actualSubtotal = Double.parseDouble(subtotalElement.getText().replace("R", ""));

        Assert.assertEquals(actualSubtotal, expectedSubtotal);

        Thread.sleep(3000);

        WebElement addressField = driver.findElement(By.id("address"));

        addressField.clear();
        addressField.sendKeys("123 Test Street");

        Assert.assertEquals(addressField.getAttribute("value"), "123 Test Street");


        driver.findElement(By.id("inventory-next-btn")).click();


    }
}
