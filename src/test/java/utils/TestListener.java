package utils;

import org.testng.ITestListener;
import org.testng.ITestResult;

import base.BaseTest;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        capture(result, "FAIL_");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        capture(result, "PASS_");
    }

    private void capture(ITestResult result, String prefix) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest bt && bt.getDriver() != null) {
            ScreenshotUtils.capture(bt.getDriver(), prefix + result.getName());
        }
    }
}