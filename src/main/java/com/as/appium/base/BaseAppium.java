package com.as.appium.base;

import com.as.appium.config.AppiumFactory;
import com.as.appium.listeners.ScreenshotListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: askeledzija
 */


@Listeners(ScreenshotListener.class)
public class BaseAppium {

    private static List<AppiumFactory> webDriverThreadPool = Collections.synchronizedList(new ArrayList<AppiumFactory>());
    private static ThreadLocal<AppiumFactory> appiumFactory;


    @BeforeSuite(alwaysRun = true)
    public static void instantiateDriverObject() {
        appiumFactory = new ThreadLocal<AppiumFactory>() {
            @Override
            protected AppiumFactory initialValue() {
                AppiumFactory appiumFactory = new AppiumFactory();
                webDriverThreadPool.add(appiumFactory);
                return appiumFactory;
            }
        };
    }

    public static WebDriver getDriver() throws Exception {
        return appiumFactory.get().getDriver();
    }

    @AfterSuite(alwaysRun = true)
    public static void closeDriverObjects() {
        for (AppiumFactory appiumFactory : webDriverThreadPool) {
            appiumFactory.quitDriver();
        }
    }

}