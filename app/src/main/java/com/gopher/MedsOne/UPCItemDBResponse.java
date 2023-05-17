package com.gopher.MedsOne;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UPCItemDBResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("total")
    private int total;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("items")
    private List<Item> items;

    public String getCode() {
        return code;
    }

    public int getTotal() {
        return total;
    }

    public int getOffset() {
        return offset;
    }

    public List<Item> getItems() {
        return items;
    }

    public static class Item {
        @JsonProperty("title")
        private String title;

        @JsonProperty("description")
        private String description;

        @JsonProperty("brand")
        private String brand;

        @JsonProperty("upc")
        private String upc;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getBrand() {
            return brand;
        }

        public String getUpc() {
            return upc;
        }
    }
}

