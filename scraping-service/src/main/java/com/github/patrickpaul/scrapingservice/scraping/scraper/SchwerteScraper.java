package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SchwerteScraper extends ProductScraper {

    private static final String schwerteStartUrl = "https://shop.schwerter-orchideenzucht.de/";
    private static final String schwerteShopNewUrl = "https://shop.schwerter-orchideenzucht.de/products_new.php";

    private static final String PRODUCT_WRAPPER = "p";

    @Override
    public List<Product> scrape() throws MalformedURLException {
        WebDriver driver = getWebDriver();

        List<Product> result = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, 30);

        try {
            driver.get(schwerteShopNewUrl);
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
        // TODO: Was ist für SCHWERTE eine geeignete Condition?
        return ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.className(PRODUCT_WRAPPER)
        );
    }

    @Override
    String getProductName(WebElement product) {
        WebElement name = product.findElement(By.tagName("u"));
        return name.getText();
    }

    @Override
    String getProductPrice(WebElement product) {
        WebElement price = product.findElement(By.xpath(".//td[2]"));
        String rawData = price.getText();

        return rawData.substring(
                        rawData.indexOf("Preis") + 6,
                        rawData.indexOf("EUR") - 1
                )
                .trim()
                .concat(" €");
    }

    @Override
    String getProductURL(WebElement product) {
        WebElement a = product.findElement(By.tagName("a"));
        // Server adds osCsid-paramter to the link, need to remove that
        String manipulatedUrl = a.getAttribute("href");

        int index = manipulatedUrl.indexOf("&");
        String correctUrl = manipulatedUrl.substring(0, index);

        return correctUrl;
    }

    @Override
    Store getProductStore() {
        return Store.SCHWERTE;
    }

}
