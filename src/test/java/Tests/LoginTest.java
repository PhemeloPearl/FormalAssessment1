package Tests;

import BasicBase.BaseTestWithWaits;
import BasicPages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTestWithWaits {

    @Test
    public void verifyLoginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(driver, wait);

        loginPage.clickLoginNav();
        loginPage.enterEmail("sandra@gmail.com");
        loginPage.enterPassword("sanDRA21#");
        loginPage.clickLoginSubmit();
        loginPage.waitForLoginModalToClose();

        Assert.assertTrue(loginPage.getWelcomeHeadingText().contains("Welcome back, Montjo!"), "Login failed - welcome message not found");
    }

}
