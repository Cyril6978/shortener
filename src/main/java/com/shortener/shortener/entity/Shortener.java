package com.shortener.shortener.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
public class Shortener {
    @Id

    private UUID id;
    private String shortId;
    private String realUrl;

    private String xRemovalToken;
    private LocalDateTime creationDate;

    public Shortener() {
    }

    @Transient
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

    public Shortener(UUID id, String shortId, String realUrl, String xRemovalToken, String creationDate) {
        this.id = id;
        this.shortId = shortId;
        this.realUrl = realUrl;
        this.xRemovalToken = xRemovalToken;
        this.creationDate = LocalDateTime.parse(creationDate, formatter);
    }

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

    public String getxRemovalToken() {
        return xRemovalToken;
    }

    public void setxRemovalToken(String xRemovalToken) {
        this.xRemovalToken = xRemovalToken;
    }


    public String getCreationDate() {
        return formatter.format(creationDate);
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = LocalDateTime.parse(creationDate, formatter);
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

}
