package pages;

import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.DebugUtils;
import utils.WaitUtils;

public class SearchResultsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Company/profile links that are NOT in the top header/nav bar
    private final By resultLinks = By.xpath(
        "//a[(contains(@href,'/company/') or contains(@href,'/in/'))" +
        " and not(ancestor::header) and not(ancestor::nav)]");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("/search/results/"));
            return true;
        } catch (TimeoutException e) {
            DebugUtils.dumpPage(driver, "results_not_loaded");
            return false;
        }
    }

    public boolean hasVisibleResult() {
        try {
            return WaitUtils.visible(driver, resultLinks, 30) != null;
        } catch (TimeoutException e) {
            DebugUtils.dumpPage(driver, "results_links_not_found");
            return false;
        }
    }

    /** Clicks the first visible result and returns its path, e.g. /company/virtusa/ */
    public String clickFirstResult() {
        try {
            WebElement first = WaitUtils.visible(driver, resultLinks, 30);
            String href = first.getAttribute("href");
            String path = new URI(href).getPath();
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", first);
            first.click();
            return path;
        } catch (TimeoutException e) {
            DebugUtils.dumpPage(driver, "click_result_failed");
            throw e;
        } catch (Exception e) {
            DebugUtils.dumpPage(driver, "click_result_failed");
            throw new RuntimeException(e);
        }
    }

    public void waitForUrlContains(String path) {
        wait.until(ExpectedConditions.urlContains(path.replaceAll("/$", "")));
    }

    /** Passes if an h1 or h2 heading is visible on the opened page. */
    public boolean isHeadingVisible() {
        try {
            return WaitUtils.visible(driver, By.cssSelector("h1, h2"), 30) != null;
        } catch (TimeoutException e) {
            DebugUtils.dumpPage(driver, "heading_not_found");
            return false;
        }
    }
}