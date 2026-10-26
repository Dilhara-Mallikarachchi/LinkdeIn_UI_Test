package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;
import pages.SearchResultsPage;
import utils.ConfigReader;
import utils.ScreenshotUtils;

public class SearchResultTest extends BaseTest {

    @Test(description = "TC04 - Open first search result")
    public void openFirstResult() {
        new LoginPage(driver).open()
            .login(ConfigReader.get("linkedin.username"), ConfigReader.get("linkedin.password"));

        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isLoaded(), "Login failed");

        SearchResultsPage results = home.search(ConfigReader.get("search.term"));
        Assert.assertTrue(results.isLoaded());
        Assert.assertTrue(results.hasVisibleResult());
        ScreenshotUtils.capture(driver, "05_results_before_click");

        String clickedPath = results.clickFirstResult();
        results.waitForUrlContains(clickedPath);

        Assert.assertTrue(driver.getCurrentUrl().contains(clickedPath.replaceAll("/$", "")),
            "Opened page does not match the clicked result");
        Assert.assertTrue(results.isHeadingVisible(), "Page heading not visible");
        ScreenshotUtils.capture(driver, "06_result_page_opened");
    }
}