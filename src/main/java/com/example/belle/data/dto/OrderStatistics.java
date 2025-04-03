package com.example.belle.data.dto;

import lombok.Getter;

@Getter
public class OrderStatistics {
    private String title;
    private long count;

    public OrderStatistics(String title, long count) {
        this.title = title;
        this.count = count;
    }
}
