package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.LinkedList;
import java.util.List;

public class KopfScraper extends ProductScraper {

    private static final String kopfStartUrl = "https://www.kopf-orchideen.de/";
    private static final String kopfShopNewUrl = "https://www.kopf-orchideen.de/Neuheiten?order=name-asc&p=1"; // page 1

    private static final String PAGES = ".pagination";
    private static final String NEXT_PAGE = ".page-next";

    private static final String PRODUCT_WRAPPER = ".product-info";
    private static final String PRODUCT_NAME = ".product-name";
    private static final String PRODUCT_PRICE = ".product-price";

    @Override
    public List<Product> scrape() {
        List<Product> result = new LinkedList<>();

        try (Playwright playwright = Playwright.create()) {
            String lastPageUrl = "";

            Browser browser = playwright.firefox().launch();
            Page page = browser.newPage();
            page.navigate(kopfShopNewUrl);

            while(!page.url().equals(lastPageUrl)) {
                page.waitForTimeout(5000); // 5 seconds
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

                lastPageUrl = page.url();
                try {
                    page.querySelector(PAGES).querySelector(NEXT_PAGE).click();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
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
                    .querySelector(PRODUCT_NAME)
                    .innerText()
                    .trim();
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    String getProductPrice(ElementHandle product) {
        String price = "error - price";
        try {
            price = product
                    .querySelector(PRODUCT_PRICE)
                    .innerText()
                    .trim()
                    .replace("*", "");
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return price;
    }

    @Override
    String getProductURL(ElementHandle product) {
        String url = "error - url";
        try {
            url = product
                    .querySelector(PRODUCT_NAME)
                    .getAttribute("href");
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    Store getProductStore() {
        return Store.KOPF;
    }
}
