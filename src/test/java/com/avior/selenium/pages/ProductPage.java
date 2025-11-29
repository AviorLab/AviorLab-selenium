package com.avior.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductPage {

    private WebDriver driver;

    @FindBy(css = ".btn.btn_primary.btn_small.btn_inventory")
    private  WebElement  addToCartButton;

    @FindBy(css = ".btn.btn_secondary.btn_small.btn_inventory")
    private  WebElement  removeButton;



    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    public void clickAddToCart() {
        addToCartButton.click();
    }

    public String getRemoveButtonText() {
        return removeButton.getText();
    }

    public WebElement getRemoveButtonElement() {
        return removeButton;
    }

}
