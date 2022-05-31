package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class WlodarczykScraper extends ProductScraper {

    private static final String wlodarczykStartUrl =
            "https://www.orchideenwlodarczyk.de/shop/catalog/index.php";
    private static final String wlodarczykShopNewUrl =
            "https://www.orchideenwlodarczyk.de/shop/catalog/products_new.php";

    private static final String PRODUCT_WRAPPER = ".item.col-sm-4.grid-group-item";
    private static final String PRODUCT_PRICE = "price";


    @Override
    public List<Product> scrape() throws MalformedURLException {
        WebDriver driver = getWebDriver();

        List<Product> result = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, 30);

        try {
            driver.get(wlodarczykShopNewUrl);
            List<WebElement> orchids = wait.until(getCondition());

            for (WebElement orchid : orchids) {
                result.add(
                        Product.createProduct(
                                getProductName(orchid),
                                getProductPrice(orchid),
                                getProductURL(orchid),
                                getProductStore()
                        )
                );
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private ExpectedCondition<List<WebElement>> getCondition() {
        return ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(PRODUCT_WRAPPER)
        );
    }

    private static void log(String s) {
        System.out.println(s);
    }

    @Override
    String getProductName(WebElement product) {
        WebElement h5 = product.findElement(By.tagName("h5"));
        return h5.getText();
    }

    @Override
    String getProductPrice(WebElement product) {
        // TODO: format price in default way
        WebElement span = product.findElement(By.className(PRODUCT_PRICE));
        return span.getText();
    }

    @Override
    String getProductURL(WebElement product) {
        WebElement a = product.findElement(By.tagName("a"));
        return a.getAttribute("href");
    }

    @Override
    Store getProductStore() {
        return Store.WLODARCZYK;
    }

}
