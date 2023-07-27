package com.shortener.shortener.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Shortener {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String shortId;
    private String realUrl;
    private String xRemovalToken;




    public Shortener(UUID id, String shortId, String realUrl) {
        this.id = id;
        this.shortId = shortId;
        this.realUrl = realUrl;
        this.xRemovalToken = xRemovalToken;
    }

    public Shortener() {

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

    public String getXRemovalToken() {
        return xRemovalToken;
    }

    public void setXRemovalToken(String xRemovalToken) {
        this.xRemovalToken = xRemovalToken;
    }
}
