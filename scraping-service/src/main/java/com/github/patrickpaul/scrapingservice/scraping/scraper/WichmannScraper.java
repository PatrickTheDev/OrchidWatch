package com.github.patrickpaul.scrapingservice.scraping.scraper;

import com.github.patrickpaul.scrapingservice.scraping.model.Product;
import com.github.patrickpaul.scrapingservice.scraping.model.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.LinkedList;
import java.util.List;

public class WichmannScraper extends ProductScraper {

    private static final String wichmannStartUrl = "https://www.orchideen-wichmann.de/";
    private static final String wichmannShopNewUrl = "https://www.orchideen-wichmann.de/de/neuheiten.html";

    private static final String TOOLBAR_TOP = ".toolbar-top";
    private static final String NEXT_PAGE_BUTTON = "a[title=\"Vor\"]";

    private static final String PRODUCT_WRAPPER = ".item";
    private static final String PRODUCT_NAME = ".product-name";

    @Override
    public List<Product> scrape() {
        List<Product> result = new LinkedList<>();

        try (Playwright playwright = Playwright.create()) {
            String nextPageUrl = wichmannShopNewUrl;

            Browser browser = playwright.firefox().launch();
            Page page = browser.newPage();
            page.navigate(nextPageUrl);
            page.waitForTimeout(5000); // 5 seconds

            while (nextPageUrl != null) {
                page.navigate(nextPageUrl);
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

                ElementHandle nextPage = page.querySelector(TOOLBAR_TOP).querySelector(NEXT_PAGE_BUTTON);
                if (nextPage != null) {
                    nextPageUrl = nextPage.getAttribute("href");
                }
                else {
                    nextPageUrl = null;
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
                    .innerText();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    String getProductPrice(ElementHandle product) {
        String price = "error - price";
        try {
            price = product
                    .querySelector("span:has-text(\"€\")")
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
        return Store.WICHMANN;
    }

}
