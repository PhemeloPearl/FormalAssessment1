package Utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserFactory {

    public static WebDriver getDriver(String browserName) {
        WebDriver driver;
        switch (browserName.toLowerCase()) {
            case "firefox":
                driver = new FirefoxDriver(new FirefoxOptions());
                break;
            case "chrome":
            default:
                driver = new ChromeDriver(new ChromeOptions());
                break;
        }
        return driver;
    }
}