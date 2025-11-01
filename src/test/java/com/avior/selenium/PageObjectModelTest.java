package com.avior.selenium;

import com.avior.selenium.pages.*;
import com.avior.selenium.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PageObjectModelTest {

    private static final String SITE = "https://www.saucedemo.com/";

    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);

        driver.get(SITE);
    }

    private static void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLogin() {
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isPageOpened(), "Login failed!");
        delay();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
