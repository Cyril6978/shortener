package com.shortener.shortener.dto;

import org.springframework.data.annotation.Transient;

import java.util.UUID;

public class ShortenerDto {
    private UUID id;
    private String shortId;
    private String realUrl;




    public ShortenerDto() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

}