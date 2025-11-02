package com.avior.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FinalCheckoutPage {

    private WebDriver driver;

    public FinalCheckoutPage(WebDriver driver) {
        this.driver = driver;
    }
    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("checkout-step-two.html");
    }
    public String getPaymentInfoValue() {
        return driver.findElement(By.cssSelector(
                ".summary_value_label[data-test='payment-info-value']")).getText();
    }
    public String getShippingInfoValue() {
        return driver.findElement(By.cssSelector(
                ".summary_value_label[data-test='shipping-info-value']")).getText();
    }

    public String getTotalLabel() {
        return driver.findElement(By.cssSelector(
                ".summary_total_label[data-test='total-label']")).getText();
    }

    public void finishCheckout() {
        driver.findElement(By.id("finish")).click();
    }
}
