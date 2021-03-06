package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.LinkedList;
import java.util.List;

public class WlodarczykScraper extends ProductScraper {

    private static final String wlodarczykStartUrl =
            "https://www.orchideenwlodarczyk.de/shop/catalog/index.php";
    private static final String wlodarczykShopNewUrl =
            "https://www.orchideenwlodarczyk.de/shop/catalog/products_new.php";

    private static final String PRODUCT_WRAPPER = ".item.col-sm-4.grid-group-item";
    private static final String PRODUCT_PRICE = ".price";

    @Override
    public List<Product> scrape() {
        List<Product> result = new LinkedList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch();
            Page page = browser.newPage();
            page.navigate(wlodarczykShopNewUrl);
            page.waitForTimeout(10000); // 10 seconds

            List<ElementHandle> orchids = page.querySelectorAll(PRODUCT_WRAPPER);
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

    @Override
    String getProductName(ElementHandle product) {
        String name = "error - name";
        try {
            name = product
                    .querySelector("h5")
                    .innerText();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    String getProductPrice(ElementHandle product) {
        // TODO: format price in default way
        String price = "error - price";
        try {
            price = product
                    .querySelector(PRODUCT_PRICE)
                    .innerText();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return price;
    }

    @Override
    String getProductURL(ElementHandle product) {
        String url = "error - url";
        try {
            url = product
                    .querySelector("a")
                    .getAttribute("href");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    Store getProductStore() {
        return Store.WLODARCZYK;
    }

}
