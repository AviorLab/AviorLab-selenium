package com.avior.selenium;

import com.avior.selenium.pages.*;
import com.avior.selenium.utils.DriverFactory;
import io.qameta.allure.testng.Tag;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.qameta.allure.*;

@Epic("Checkout flow on Saucedemo")
@Owner("Avior Kasay")


public class CheckoutFlowTest {

    private static final String SITE = "https://www.saucedemo.com/";

    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private FinalCheckoutPage finalCheckoutPage;
    private OrderCompletionPage orderCompletionPage;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        finalCheckoutPage = new FinalCheckoutPage(driver);
        orderCompletionPage = new OrderCompletionPage(driver);

        driver.get(SITE);
    }

    private static void delay() {
        try {
            Thread.sleep(1500); // מינימום כדי לא לחפור עכשיו על waits
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Feature("Checkout flow")
    @Story("Complete purchase")
    @Description("Login -> Add 2 items -> Cart -> Checkout -> Finish -> Validate success")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("checkout")
    @Test
    public void completeCheckoutFlow() {

        Allure.step("Login", () -> {
            loginPage.login("standard_user", "secret_sauce");
            Assert.assertTrue(productsPage.isPageOpened(), "Login failed!");
        });
        delay();

        Allure.step("Reset state (optional) + Add Backpack", () -> {
            productsPage.resetState(); // אם אין לך, תגיד לי ואסיר/אחליף
            productsPage.navigateToProductPage("Sauce Labs Backpack");
            productPage.addToCart();
            Assert.assertEquals(productPage.getButtonText(), "Remove", "Backpack was not added");
        });
        delay();
        driver.navigate().back();

        Allure.step("Add Fleece Jacket", () -> {
            productsPage.navigateToProductPage("Sauce Labs Fleece Jacket");
            productPage.addToCart();
            Assert.assertEquals(productPage.getButtonText(), "Remove", "Jacket was not added");
        });
        delay();
        driver.navigate().back();

        Allure.step("Open cart + Verify items", () -> {
            productsPage.navigateToCart();
            Assert.assertTrue(cartPage.isPageOpened(), "Cart page not loaded");
            Assert.assertEquals(cartPage.getCartItemCount(), "2", "Incorrect number of items in the cart");

            Assert.assertTrue(cartPage.productInCart("Sauce Labs Backpack"), "Backpack not in cart");
            Assert.assertTrue(cartPage.productInCart("Sauce Labs Fleece Jacket"), "Jacket not in cart");
        });
        delay();

        Allure.step("Checkout - Fill details", () -> {
            cartPage.continueCheckout();
            Assert.assertTrue(checkoutPage.isPageOpened(), "Checkout page not loaded");

            checkoutPage.enterDetails("Peter", "Hank", "12345");

            // אם אין לך getters לשדות - תוריד את ה-asserts האלה (תגיד לי)
            Assert.assertEquals(checkoutPage.getFirstNameFieldValue(), "Peter");
            Assert.assertEquals(checkoutPage.getLastNameFieldValue(), "Hank");
            Assert.assertEquals(checkoutPage.getZipCodeFieldValue(), "12345");
        });
        delay();

        Allure.step("Final checkout - Validate summary", () -> {
            checkoutPage.continueCheckout();
            Assert.assertTrue(finalCheckoutPage.isPageOpened(), "Checkout summary not loaded");

            Assert.assertEquals(finalCheckoutPage.getPaymentInfoValue(), "SauceCard #31337");
            Assert.assertEquals(finalCheckoutPage.getShippingInfoValue(), "Free Pony Express Delivery!");
            Assert.assertEquals(finalCheckoutPage.getTotalLabel(), "Total: $86.38");
        });
        delay();

        Allure.step("Finish + Validate order completion", () -> {
            finalCheckoutPage.finishCheckout();
            Assert.assertEquals(orderCompletionPage.getHeaderText(), "Thank you for your order!");
        });
        delay();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
