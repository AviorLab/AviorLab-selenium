package com.avior.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage {
    private WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("cart.html");
    }

    public String getCartItemCount() {
        return driver.findElement(By.cssSelector(".shopping_cart_badge")).getText();
    }

    public boolean productInCart(String productName) {
        WebElement product = driver.findElement(By.xpath(
                String.format("//div[@class='inventory_item_name'][text()='%s']", productName)
        ));
        return product != null;
    }

    public String getContinueButtonText() {
        return driver.findElement(By.cssSelector(".btn_action")).getText();
    }

    public void continueCheckout() {
        driver.findElement(By.cssSelector(".btn_action")).click();
    }
}
