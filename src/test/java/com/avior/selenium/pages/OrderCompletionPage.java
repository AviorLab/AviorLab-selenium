package com.avior.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OrderCompletionPage {

    private WebDriver driver;

    // ===== Locators using @FindBy =====
    @FindBy(css = "[data-test='complete-header']")
    private WebElement completeHeader;

    @FindBy(css = "[data-test='complete-text']")
    private WebElement completeText;

    // ===== Constructor =====
    public OrderCompletionPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ===== Page Actions =====
    public String getHeaderText() {
        return completeHeader.getText();
    }

    public String getBodyText() {
        return completeText.getText();
    }
}
