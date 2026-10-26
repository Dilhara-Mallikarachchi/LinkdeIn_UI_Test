package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.DebugUtils;
import utils.WaitUtils;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By searchBox = By.cssSelector(
        "input[data-testid='typeahead-input'], input[placeholder='Search']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        // Long wait so you have time to solve a CAPTCHA/verification MANUALLY if shown
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(120));
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("/feed"));
            WaitUtils.visible(driver, searchBox, 30);
            return true;
        } catch (TimeoutException e) {
            DebugUtils.dumpPage(driver, "home_not_loaded");
            return false;
        }
    }

    public SearchResultsPage search(String term) {
        try {
            WebElement box = WaitUtils.visible(driver, searchBox, 30);
            box.click();
            box.clear();
            box.sendKeys(term);
            box.sendKeys(Keys.ENTER);
        } catch (TimeoutException e) {
            DebugUtils.dumpPage(driver, "search_box_not_found");
            throw e;
        }
        return new SearchResultsPage(driver);
    }
}