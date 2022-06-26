package com.github.patrickpaul.scrapingservice.scraping.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Product {

    String name;
    String url;
    String price;
    Store store;

    public Product(String name, String price, String url, Store store) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.store = store;
    }

    public static Product createProduct(
            String name, String price, String url, Store store
    ) {
        return new Product(name, price, url, store);
    }

    //Getters
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public String getPrice() {
        return price;
    }
    public Store getStore() {
        return store;
    }
    //Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setStore(Store store) {
        this.store = store;
    }

    public String toPrintString() {
        StringBuilder sb = new StringBuilder()
                .append(name).append("\n")
                .append("Preis: ").append(price).append("\n")
                .append("Store: ").append(store).append("\n")
                .append("URL: ").append(url).append("\n");

        return sb.toString();
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(this);
        } catch(JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return jsonString;
    }
}
