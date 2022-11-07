package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class eShopTests {

    private WebDriver chromeDriver;

    private static final String baseUrl = "http://automationpractice.com/index.php";


    @Before
    public void setUp() {
        //Run driver
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        //Set fullscreen view
        chromeOptions.addArguments("--start-fullscreen");
        //setup waiting for loading
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
        chromeDriver.get(baseUrl);
    }

    @Test
    public void testClickCategory() {
        WebElement item = chromeDriver.findElement(By.xpath("//ul[@class='sf-menu clearfix menu-content sf-js-enabled sf-arrows']/li[3]/a"));
        Assert.assertNotNull(item);
        item.click();
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchField(){

        WebElement searchField = chromeDriver.findElement(By.id("search_query_top"));
        Assert.assertNotNull(searchField);

        //input text in search
        String inputValue = "dress";
        searchField.sendKeys(inputValue);
        Assert.assertEquals(inputValue, searchField.getAttribute("value"));

        //click Enter
        searchField.sendKeys(Keys.ENTER);
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testOpenProduct(){
        WebElement page = chromeDriver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[1]/ul[1]/li[1]/div/div[1]/div/a[1]"));
        Assert.assertNotNull(page);
        chromeDriver.get(page.getAttribute("href"));
        Assert.assertEquals("http://automationpractice.com/index.php?id_product=1&controller=product", chromeDriver.getCurrentUrl());
    }

    @Test
    public void testAddToCart() {
        chromeDriver.get("http://automationpractice.com/index.php?id_product=1&controller=product");

        WebElement addToCart = chromeDriver.findElement(By.xpath("//p[@id='add_to_cart']/button"));
        Assert.assertNotNull(addToCart);
        addToCart.click();

        if(chromeDriver.getPageSource().contains("empty")){
            System.out.println("Your cart is empty");
        }
        else{
            System.out.println("Your order was placed");
        }

        WebElement checkout = chromeDriver.findElement(By.xpath("//a[@title='Proceed to checkout']"));
        Assert.assertNotNull(checkout);
        checkout.click();
        Assert.assertEquals("http://automationpractice.com/index.php?controller=order", chromeDriver.getCurrentUrl());
    }

    @After()
    public void tearDown() {
        chromeDriver.quit();
    }

}

