package BasicPages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Locators ──────────────────────────────────────
    private By loginNavBtn     = By.xpath("//*[@id=\"app-root\"]/nav/div[1]/div[3]/button/span[2]");
    private By emailField      = By.id("login-email");
    private By passwordField   = By.id("login-password");
    private By loginSubmitBtn  = By.id("login-submit");
    private By welcomeHeading  = By.xpath("//h2[contains(normalize-space(.), 'Welcome back')]");
    private By menuBtn         = By.xpath("//*[@id=\"app-root\"]/nav/div[1]/div[2]/div[1]/button/span[2]");
    private By practiceLink    = By.cssSelector(
            "#app-root > nav > div.nav-container > div.nav-items > div:nth-child(4) > div > button:nth-child(2)");
    private By practiceHeading = By.id("practice-heading");
    private By webTab          = By.id("tab-btn-web");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    public void clickLoginNav() {
        wait.until(ExpectedConditions.elementToBeClickable(loginNavBtn)).click();
    }

    public void enterEmail(String email) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        el.click();
        el.clear();
        el.sendKeys(email);
        jsSetValue(el, email);
    }

    public void enterPassword(String password) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        el.click();
        el.clear();
        el.sendKeys(password);
        jsSetValue(el, password);
        el.sendKeys(Keys.RETURN);
    }

    public void clickLoginSubmit() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginSubmitBtn)).click();
        } catch (Exception e) {
            // Modal already closed via Enter key
        }
    }

    public void waitForLoginModalToClose() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(emailField));
    }

    public String getWelcomeHeadingText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeHeading)).getText();
    }

    public void clickMenuButton() {
        wait.until(ExpectedConditions.elementToBeClickable(menuBtn)).click();
    }

    public void clickPracticeLink() {
        wait.until(ExpectedConditions.elementToBeClickable(practiceLink)).click();
    }

    public String getPracticeHeadingText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(practiceHeading)).getText();
    }

    public void clickWebTab() {
        wait.until(ExpectedConditions.elementToBeClickable(webTab)).click();
    }

    private JavascriptExecutor js() { return (JavascriptExecutor) driver; }

    private void jsSetValue(WebElement el, String value) {
        js().executeScript("var el = arguments[0]; var setter = Object.getOwnPropertyDescriptor(" + "window.HTMLInputElement.prototype, 'value').set;" + "setter.call(el, arguments[1]); el.dispatchEvent(new Event('input', { bubbles: true }));", el, value);
    }
}
