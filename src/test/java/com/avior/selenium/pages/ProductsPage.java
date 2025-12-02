package com.avior.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class ProductsPage {
    private  WebDriver driver;

    @FindBy (css = ".shopping_cart_link")
    private  WebElement  cartButton;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }
    public void resetState() {
        // פותח את התפריט
        WebElement menuBtn = driver.findElement(By.id("react-burger-menu-btn"));
        menuBtn.click();

        // מחכה שהכפתור של reset יהיה באמת קליקבילי
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement resetLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("reset_sidebar_link"))
        );

        // לוחץ על RESET
        resetLink.click();
    }
    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("inventory.html");
    }

    public void navigateToProductPage(String productName) {
        WebElement productLink = driver.findElement(By.linkText(productName));
        productLink.click();
    }

    public void navigateToCart() {
        cartButton.click();
    }
}