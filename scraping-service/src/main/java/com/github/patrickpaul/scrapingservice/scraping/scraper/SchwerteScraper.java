package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.LinkedList;
import java.util.List;


public class SchwerteScraper extends ProductScraper {

    private static final String schwerteStartUrl = "https://shop.schwerter-orchideenzucht.de/";
    private static final String schwerteShopNewUrl = "https://shop.schwerter-orchideenzucht.de/products_new.php";

    private static final String PRODUCT_WRAPPER = ".p";

    @Override
    public List<Product> scrape() {
        List<Product> result = new LinkedList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch();
            Page page = browser.newPage();
            page.navigate(schwerteShopNewUrl);
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
                    .querySelector("u")
                    .innerText();
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    String getProductPrice(ElementHandle product) {
        String price = "error - price";
        try {
            String rawData = product
                    .querySelector("td:has-text(\"Preis\")")
                    .innerText();
            price = rawData
                    .substring(
                            rawData.indexOf("Preis") + 6,
                            rawData.indexOf("EUR") - 1
                    )
                    .trim()
                    .concat(" ???");
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return price;
    }

    @Override
    String getProductURL(ElementHandle product) {
        String price = "error - price";
        try {
            String manipulatedUrl = product.querySelector("a").getAttribute("href");
            int index = manipulatedUrl.indexOf("&");
            price = manipulatedUrl.substring(0, index);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return price;
    }

    @Override
    Store getProductStore() {
        return Store.SCHWERTE;
    }
}
