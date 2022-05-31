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
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CramerScraper extends ProductScraper {

    private static final String cramerStartUrl = "https://www.cramer-orchideen.de/index.php";
    private static final String cramerShopNewUrl =
            "https://www.cramer-orchideen.de/shop.php#!/Neu-im-Online-Gew%C3%A4chshaus/c/83688433";

    private static final String PRODUCT_WRAPPER = "grid-product__wrap-inner";
    private static final String PRODUCT_TITLE = "grid-product__title-inner";
    private static final String PRODUCT_PRICE = ".grid-product__price-value.ec-price-item";
    private static final String PRODUCT_URL = "grid-product__title";

    private static final String SOLD_LABEL = ".ec-label.label--attention";

    @Override
    public List<Product> scrape() throws MalformedURLException {
        WebDriver driver = getWebDriver();

        List<Product> result = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, 30);

        try {
            driver.get(cramerShopNewUrl);
            List<WebElement> orchids = wait.until(getCondition());

            /*
            Some new orchids may already be sold at the time scraping
            them, so don't add them to DB and potentially show them in the
            OrchidWatch Android application.
             */
            List<WebElement> soldOrchids = getSoldOrchids(orchids);
            orchids.removeAll(soldOrchids);

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

    /**
     * Manchmal ist die Seite leer, weil es keine neuen Orchideen gab.
     * @return expectation for checking that there is at least one element present
     *         on the website, e.g. an orchid that can be scraped
     */
    private ExpectedCondition<List<WebElement>> getCondition() {
        return ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.className(PRODUCT_WRAPPER)
        );
    }

    private List<WebElement> getSoldOrchids(List<WebElement> orchids) {
        return orchids.stream()
                .filter(orchid ->
                        (orchid.findElements(By.cssSelector(SOLD_LABEL)).size() > 0)
                )
                .collect(Collectors.toList());
    }

    @Override
    public String getProductName(WebElement product) {
        WebElement name = product.findElement(By.className(PRODUCT_TITLE));
        return name.getText();
    }

    @Override
    public String getProductURL(WebElement product) {
        WebElement url = product.findElement(By.className(PRODUCT_URL));
        return url.getAttribute("href");
    }

    @Override
    public String getProductPrice(WebElement product) {
        WebElement price = product.findElement(By.cssSelector(PRODUCT_PRICE));
        return price.getText()
                .replace(",", ".");
    }

    @Override
    public Store getProductStore() {
        return Store.CRAMER;
    }

}
