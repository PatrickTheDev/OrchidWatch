package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CramerScraper extends ProductScraper {

    private static final String cramerStartUrl = "https://www.cramer-orchideen.de/index.php";
    private static final String cramerShopUrl = "https://www.cramer-orchideen.de/shop.php";
    private static final String cramerShopNewUrl =
            "https://www.cramer-orchideen.de/shop.php#!/Neu-im-Online-Gew%C3%A4chshaus/c/83688433";

    private static final String PRODUCT_WRAPPER = ".grid-product__wrap-inner";
    private static final String PRODUCT_TITLE = ".grid-product__title-inner";
    private static final String PRODUCT_PRICE = ".grid-product__price-value.ec-price-item";
    private static final String PRODUCT_URL = ".grid-product__title";

    private static final String SOLD_LABEL = ".ec-label.label--attention";

    @Override
    public List<Product> scrape() {
        List<Product> result = new LinkedList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch();
            Page page = browser.newPage();
            page.navigate(cramerShopNewUrl);
            page.waitForTimeout(10000); // 10 seconds

            List<ElementHandle> orchids = page.querySelectorAll(PRODUCT_WRAPPER);
            orchids.removeAll(getSoldOrchids(orchids));
            for (ElementHandle orchid : orchids) {
                result.add(
                        Product.createProduct(
                                getProductName(orchid),
                                getProductPrice(orchid),
                                getProductURL(orchid),
                                getProductStore()
                        )
                );
            }

            browser.close();
        }

        return result;
    }

    private List<ElementHandle> getSoldOrchids(List<ElementHandle> orchids) {
        return orchids.stream()
                .filter(orchid ->
                        (orchid.querySelectorAll(SOLD_LABEL).size() > 0)
                )
                .collect(Collectors.toList());
    }

    @Override
    public String getProductName(ElementHandle product) {
        String name = "error - name";
        try {
            name = product
                    .querySelector(PRODUCT_TITLE)
                    .innerText();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public String getProductURL(ElementHandle product) {
        String url = "error - url";
        try {
            url = cramerShopUrl + product
                    .querySelector(PRODUCT_URL)
                    .getAttribute("href");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public String getProductPrice(ElementHandle product) {
        String price = "error - price";
        try {
            price = product
                    .querySelector(PRODUCT_PRICE)
                    .innerText()
                    .replace(",", ".");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return price;
    }

    @Override
    public Store getProductStore() {
        return Store.CRAMER;
    }

}
