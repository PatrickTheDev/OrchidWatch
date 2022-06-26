package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.LinkedList;
import java.util.List;

public class OrchidHouseScraper extends ProductScraper {

    private static final String orchidHouseStartUrl =
            "https://orchidhouseasia.com/";
    private static final String orchidHouseShopNewUrl =
            "https://orchidhouseasia.com/shop/?avia_extended_shop_select=yes&product_order=date";

    private static final String PRODUCT_WRAPPER = ".inner_product";
    private static final String PRODUCT_NAME = ".woocommerce-loop-product__title";
    private static final String PRODUCT_PRICE = ".woocommerce-Price-amount";

    @Override
    public List<Product> scrape() {
        List<Product> result = new LinkedList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch();
            Page page = browser.newPage();
            page.navigate(orchidHouseShopNewUrl);
            page.waitForTimeout(5000); // 10 seconds

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
                    .querySelector(PRODUCT_NAME)
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
            price = product
                    .querySelector(PRODUCT_PRICE)
                    .querySelector("bdi")
                    .innerText();
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
                    .querySelector("a")
                    .getAttribute("href");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    Store getProductStore() {
        return Store.ORCHID_HOUSE;
    }
}
