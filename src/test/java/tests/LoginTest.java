package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.ScreenshotUtils;

public class LoginTest extends BaseTest {

    @Test(description = "TC01 - Valid login")
    public void validLogin() {
        LoginPage login = new LoginPage(driver).open();
        ScreenshotUtils.capture(driver, "01_login_page");

        login.login(ConfigReader.get("linkedin.username"), ConfigReader.get("linkedin.password"));

        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isLoaded(), "Feed page / search box not displayed after login");
        ScreenshotUtils.capture(driver, "02_home_after_login");
        Assert.assertTrue(driver.getCurrentUrl().contains("/feed"));
    }

    @Test(description = "TC02 - Invalid password shows error")
    public void invalidLogin() {
        LoginPage login = new LoginPage(driver).open();
        login.login(ConfigReader.get("linkedin.username"), "WrongPassword@123");

        String error = login.getErrorText();
        ScreenshotUtils.capture(driver, "03_invalid_login_error");
        Assert.assertFalse(error.isBlank(), "Expected an error message");
    }
}