import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
        driver.findElement(By.id("deviceType")).click();
        boolean brandDropDown = driver.findElement(By.id("deviceType")).isDisplayed();
        Assert.assertTrue(brandDropDown);
        driver.findElement(By.xpath("//*[@id=\"deviceType\"]/option[2]")).click();


    }
}