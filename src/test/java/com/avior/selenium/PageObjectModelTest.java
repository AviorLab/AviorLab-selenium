package com.avior.selenium;

import com.avior.selenium.pages.*;
import com.avior.selenium.utils.DriverFactory;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.qameta.allure.*;

import java.time.Duration;

@Epic("Checkout flow on Saucedemo")
@Owner("Avior Kasay")
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Feature("Login flow")
    @Story("Login")
    @Description("Test to verify login functionality")
    @Severity(SeverityLevel.BLOCKER)
    @Link(name = "Saucedemo", url = "https://www.saucedemo.com/")
    @Tag("login")
    @Step("Perform login with valid credentials")
    @Test
    public void testLogin() {
        Allure.step("Navigate to login page and perform login", () -> {
            loginPage.login("standard_user", "secret_sauce");
            Assert.assertTrue(productsPage.isPageOpened(), "Login failed!");
        });
        delay();
    }

    @Feature("Add products flow")
    @Story("Add products")
    @Description("Test to add a backpack to the cart")
    @Severity(SeverityLevel.NORMAL)
    @Tags({@Tag("add"), @Tag("cart")})
    @Owner("Avior Kasay")
    @Step("Add backpack to cart and verify")
    @Test(dependsOnMethods = "testLogin")

    public void testAddBackpackToCart() {
        Allure.step("Navigate to product and add to cart", () -> {
            productsPage.navigateToProductPage("Sauce Labs Backpack");
            productPage.addToCart();
            Assert.assertEquals(productPage.getButtonText(), "Remove", "Button text did not change");
            Allure.addAttachment("Product added", "Sauce Labs Backpack");
        });
        delay();
        driver.navigate().back();
    }

    @Feature("Add products flow")
    @Story("Add products")
    @Description("Test to add a fleece jacket to the cart")
    @Severity(SeverityLevel.NORMAL)
    @Tags({@Tag("add"), @Tag("cart")})
    @Step("Add fleece jacket to cart and verify")
    @Test(dependsOnMethods = "testAddBackpackToCart")

    public void testAddFleeceJacketToCart() {
        Allure.step("Navigate to fleece jacket and add to cart", () -> {
            productsPage.navigateToProductPage("Sauce Labs Fleece Jacket");
            productPage.addToCart();
            Assert.assertEquals(productPage.getButtonText(), "Remove", "Button text did not change");
            Allure.addAttachment("Product added", "Sauce Labs Fleece Jacket");
        });
        delay();
        driver.navigate().back();
    }

    @Feature("View cart flow")
    @Story("View cart")
    @Description("Test to verify the cart contents")
    @Link(name = "Saucedemo cart", url = "https://www.saucedemo.com/cart.html")
    @Severity(SeverityLevel.CRITICAL)
    @Tags({@Tag("cart"), @Tag("checkout")})
    @Owner("Avior Kasay")
    @Step("Validate items in cart")
    @Test(dependsOnMethods = {"testAddBackpackToCart", "testAddFleeceJacketToCart"})

    public void testCart() {
        Allure.step("Navigate to cart and verify state", () -> {
            productsPage.navigateToCart();
            Assert.assertTrue(cartPage.isPageOpened(), "Cart page not loaded");
            Assert.assertEquals(cartPage.getCartItemCount(), "2", "Incorrect number of items in the cart");
            Assert.assertEquals(cartPage.getContinueButtonText(), "Checkout", "Incorrect button text on the cart page");
            Allure.addAttachment("Cart item count", cartPage.getCartItemCount());
        });

        Allure.step("Verify products in cart", () -> {
            Assert.assertTrue(cartPage.productInCart("Sauce Labs Backpack"));
            Assert.assertTrue(cartPage.productInCart("Sauce Labs Fleece Jacket"));
        });

        delay();
    }

    @Feature("Checkout flow")
    @Story("Checkout")
    @Description("Test to verify the checkout functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Tag("checkout")
    @Owner("Avior Kasay")
    @Step("Fill checkout information and verify details")
    @Test(dependsOnMethods = "testCart")

    public void testCheckout() {
        Allure.step("Proceed to checkout page and fill details", () -> {
            cartPage.continueCheckout();
            Assert.assertTrue(checkoutPage.isPageOpened(), "Checkout page not loaded");
            checkoutPage.enterDetails("Peter", "Hank", "12345");
            Assert.assertEquals(checkoutPage.getFirstNameFieldValue(), "Peter");
            Assert.assertEquals(checkoutPage.getLastNameFieldValue(), "Hank");
            Assert.assertEquals(checkoutPage.getZipCodeFieldValue(), "12345");
        });
        delay();
    }

    @Feature("Checkout flow")
    @Story("Final checkout")
    @Description("Verify final checkout summary and payment info")
    @Severity(SeverityLevel.NORMAL)
    @Flaky
    @Owner("Avior Kasay")
    @Step("Review final checkout summary")
    @Test(dependsOnMethods = "testCheckout")
    public void testFinalCheckout() {
        Allure.step("Navigate to final checkout summary", () -> {
            checkoutPage.continueCheckout();
            Assert.assertTrue(finalCheckoutPage.isPageOpened(), "Checkout summary not loaded");
            Assert.assertEquals(finalCheckoutPage.getPaymentInfoValue(), "SauceCard #31337");
            Assert.assertEquals(finalCheckoutPage.getShippingInfoValue(), "Free Pony Express Delivery!");
            Assert.assertEquals(finalCheckoutPage.getTotalLabel(), "Total: $86.38");
        });
        delay();
    }

    @Feature("Checkout flow")
    @Story("Order completion")
    @Description("Test to verify order completion message")
    @Severity(SeverityLevel.MINOR)
    @Owner("Avior Kasay")
    @Tag("checkout")
    @Step("Complete order and validate success message")
    @Test(dependsOnMethods = "testFinalCheckout")
    public void testOrderCompletion() {
        Allure.step("Finish checkout process", () -> {
            finalCheckoutPage.finishCheckout();
            Assert.assertEquals(orderCompletionPage.getHeaderText(), "Thank you for your order!");
            Assert.assertEquals(orderCompletionPage.getBodyText(),
                    "Your order has been dispatched, and will arrive just as fast as the pony can get there!");
        });
        delay();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
