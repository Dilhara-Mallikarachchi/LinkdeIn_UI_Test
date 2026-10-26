package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.DebugUtils;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // LinkedIn renders duplicate (hidden) fields, so we always pick the DISPLAYED match.
    private final By usernameField = By.cssSelector("input[autocomplete='username']");
    private final By passwordField = By.cssSelector("input[type='password']");
    // Exact text match, so "Sign in with Apple/Microsoft" are not selected
    private final By signInButton  = By.xpath("//button[normalize-space(.)='Sign in']");
    private final By errorMessage  = By.cssSelector(
        "[role='alert'], [id*='error'], [class*='error'], [data-testid*='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /** Waits until at least one element matching 'by' is displayed and returns it. */
    private WebElement visible(By by) {
        return wait.until(d -> {
            List<WebElement> all = d.findElements(by);
            for (WebElement e : all) {
                try {
                    if (e.isDisplayed()) return e;
                } catch (Exception ignored) { }
            }
            return null;
        });
    }

    public LoginPage open() {
        driver.get("https://www.linkedin.com/login");
        try {
            visible(passwordField);
        } catch (TimeoutException e) {
            DebugUtils.dumpPage(driver, "login_not_found");
            throw e;
        }
        return this;
    }

    public void login(String user, String pass) {
        WebElement u = visible(usernameField);
        u.clear();
        u.sendKeys(user);

        WebElement p = visible(passwordField);
        p.clear();
        p.sendKeys(pass);

        try {
            visible(signInButton).click();
        } catch (Exception e) {
            DebugUtils.dumpPage(driver, "signin_button_not_found");
            p.sendKeys(Keys.ENTER);   // fallback: press Enter in the password field
        }
    }

    public String getErrorText() {
        try {
            return wait.until(d -> {
                for (WebElement e : d.findElements(errorMessage)) {
                    try {
                        if (e.isDisplayed() && !e.getText().isBlank()) return e.getText();
                    } catch (Exception ignored) { }
                }
                return null;
            });
        } catch (TimeoutException e) {
            DebugUtils.dumpPage(driver, "error_not_found");
            throw e;
        }
    }
}