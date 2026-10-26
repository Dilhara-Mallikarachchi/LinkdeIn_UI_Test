package utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {

    /** Waits until at least one element matching 'by' is displayed and returns it. */
    public static WebElement visible(WebDriver driver, By by, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d -> {
            List<WebElement> all = d.findElements(by);
            for (WebElement e : all) {
                try {
                    if (e.isDisplayed()) return e;
                } catch (Exception ignored) { }
            }
            return null;
        });
    }
}