package utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DebugUtils {

    /** Saves the page HTML and prints every <input> and <button> it can see. */
    public static void dumpPage(WebDriver driver, String name) {
        try {
            Files.createDirectories(Paths.get("debug"));
            Files.writeString(Paths.get("debug", name + ".html"), driver.getPageSource());
            System.out.println("=== DEBUG DUMP (" + name + ") ===");
            System.out.println("URL   : " + driver.getCurrentUrl());
            System.out.println("TITLE : " + driver.getTitle());

            List<WebElement> inputs = driver.findElements(By.tagName("input"));
            System.out.println("INPUTS found: " + inputs.size());
            for (WebElement e : inputs) {
                System.out.println("  input id=" + e.getAttribute("id")
                        + " name=" + e.getAttribute("name")
                        + " type=" + e.getAttribute("type")
                        + " autocomplete=" + e.getAttribute("autocomplete")
                        + " data-testid=" + e.getAttribute("data-testid")
                        + " displayed=" + e.isDisplayed());
            }
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            System.out.println("BUTTONS found: " + buttons.size());
            for (WebElement b : buttons) {
                System.out.println("  button type=" + b.getAttribute("type")
                        + " text=[" + b.getText() + "]"
                        + " data-testid=" + b.getAttribute("data-testid"));
            }
            System.out.println("=== END DEBUG ===");
        } catch (Exception ex) {
            System.out.println("Debug dump failed: " + ex.getMessage());
        }
    }
}