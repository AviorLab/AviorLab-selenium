package com.avior.selenium.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderCompletionPage {
    private WebDriver driver;

    public OrderCompletionPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getHeaderText() {
        return driver.findElement(By.cssSelector("[data-test='complete-header']")).getText();
    }

    public String getBodyText() {
        return driver.findElement(By.cssSelector("[data-test='complete-text']")).getText();
    }
}
