package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;
import pages.SearchResultsPage;
import utils.ConfigReader;
import utils.ScreenshotUtils;

public class SearchTest extends BaseTest {

    @Test(description = "TC03 - Search displays results page")
    public void searchShowsResults() {
        new LoginPage(driver).open()
            .login(ConfigReader.get("linkedin.username"), ConfigReader.get("linkedin.password"));

        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isLoaded(), "Login failed");

        SearchResultsPage results = home.search(ConfigReader.get("search.term"));
        Assert.assertTrue(results.isLoaded(), "Not on search results URL");
        Assert.assertTrue(driver.getCurrentUrl().contains("/search/results/"));
        Assert.assertTrue(results.hasVisibleResult(), "No visible result element");
        ScreenshotUtils.capture(driver, "04_search_results");
    }
}