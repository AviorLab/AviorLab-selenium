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
    @Test(dependsOnMethods = "testLogin")
    public void testAddBackpackToCart() {
        productsPage.navigateToProductPage("Sauce Labs Backpack");
        productPage.addToCart();
        Assert.assertEquals(productPage.getButtonText(), "Remove", "Button text did not change");
        delay();
        driver.navigate().back();
    }

    @Test(dependsOnMethods = "testAddBackpackToCart")
    public void testAddFleeceJacketToCart() {
        productsPage.navigateToProductPage("Sauce Labs Fleece Jacket");
        productPage.addToCart();
        Assert.assertEquals(productPage.getButtonText(), "Remove", "Button text did not change");
        delay();
        driver.navigate().back();
    }

    @Test(dependsOnMethods = {"testAddBackpackToCart", "testAddFleeceJacketToCart"})
    public void testCart() {
        productsPage.navigateToCart();

        Assert.assertTrue(cartPage.isPageOpened(), "Cart page not loaded");
        Assert.assertEquals(cartPage.getCartItemCount(), "2", "Incorrect number of items in the cart");
        Assert.assertEquals(cartPage.getContinueButtonText(), "Checkout", "Incorrect button text on the cart page");

        Assert.assertTrue(cartPage.productInCart("Sauce Labs Backpack"));
        Assert.assertTrue(cartPage.productInCart("Sauce Labs Fleece Jacket"));

        delay();
    }
    @Test(dependsOnMethods = "testCart")
    public void testCheckout() {
        cartPage.continueCheckout();

        Assert.assertTrue(checkoutPage.isPageOpened(), "Checkout page not loaded");
        checkoutPage.enterDetails("Peter", "Hank", "12345");

        Assert.assertEquals(checkoutPage.getFirstNameFieldValue(), "Peter", "First name field value is incorrect");
        Assert.assertEquals(checkoutPage.getLastNameFieldValue(), "Hank", "Last name field value is incorrect");
        Assert.assertEquals(checkoutPage.getZipCodeFieldValue(), "12345", "Zip code field value is incorrect");

        delay();
    }
    @Test(dependsOnMethods = "testCheckout")
    public void testFinalCheckout() {
        checkoutPage.continueCheckout();

        Assert.assertTrue(finalCheckoutPage.isPageOpened(), "Checkout page not loaded");
        Assert.assertEquals(finalCheckoutPage.getPaymentInfoValue(), "SauceCard #31337");
        Assert.assertEquals(finalCheckoutPage.getShippingInfoValue(), "Free Pony Express Delivery!");
        Assert.assertEquals(finalCheckoutPage.getTotalLabel(), "Total: $86.38");

        delay();
    }

    @Test(dependsOnMethods = "testFinalCheckout")
    public void testOrderCompletion() {
        finalCheckoutPage.finishCheckout();

        Assert.assertEquals(orderCompletionPage.getHeaderText(), "Thank you for your order!");
        Assert.assertEquals(orderCompletionPage.getBodyText(),
                "Your order has been dispatched, and will arrive just as fast as the pony can get there!");

        delay();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
