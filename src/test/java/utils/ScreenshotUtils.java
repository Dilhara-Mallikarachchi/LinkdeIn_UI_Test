package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

    public static void capture(WebDriver driver, String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(Paths.get("screenshots"));
            Path dest = Paths.get("screenshots", name + "_" + System.currentTimeMillis() + ".png");
            Files.copy(src.toPath(), dest);
            System.out.println("Screenshot saved: " + dest);
        } catch (Exception e) {
            System.out.println("Could not take screenshot: " + e.getMessage());
        }
    }
}