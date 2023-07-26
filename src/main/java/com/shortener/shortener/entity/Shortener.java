package com.shortener.shortener.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.UUID;

@Entity
public class Shortener {

    @Id

    private UUID id;
    private String shortId;
    private String realUrl;

    private String xRemovalToken;

    public Shortener() {
    }


    public Shortener(UUID id, String shortId, String realUrl, String xRemovalToken) {

        this.id = id;
        this.shortId = shortId;
        this.realUrl = realUrl;
        this.xRemovalToken = xRemovalToken;
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
}

