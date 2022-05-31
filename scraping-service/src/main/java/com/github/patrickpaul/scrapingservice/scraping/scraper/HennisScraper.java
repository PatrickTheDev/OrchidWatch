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

public class HennisScraper extends ProductScraper {

    private static final String hennisStartUrl = "https://hennis-orchideen.de/";
    private static final String hennisShopNewUrl =
            "https://hennis-orchideen.de/collections/alle-produkte?sort_by=created-descending";

    private static final String PRODUCT_WRAPPER = "grid-item__content";
    private static final String PRODUCT_NAME = "grid-product__title";
    private static final String PRODUCT_PRICE = "grid-product__price--current";

    @Override
    public List<Product> scrape() throws MalformedURLException {
        WebDriver driver = getWebDriver();

        List<Product> result = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, 30);

        try {
            driver.get(hennisShopNewUrl);
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
                By.className(PRODUCT_WRAPPER)
        );
    }

    private static void log(String s) {
        System.out.println(s);
    }

    @Override
    String getProductName(WebElement product) {
        WebElement div = product.findElement(By.className(PRODUCT_NAME));
        return div.getText();
    }

    @Override
    String getProductPrice(WebElement product) {
        /*
        Preis scheint nicht im headless-mode geladen angezeigt zu werden und
        ist somit nicht für den Scraper zu finden.
         */
        return "Hennis: Scraping the price not possible";
    }

    @Override
    String getProductURL(WebElement product) {
        WebElement a = product.findElement(By.tagName("a"));
        return a.getAttribute("href");
    }

    @Override
    Store getProductStore() {
        return Store.HENNIS;
    }

}
