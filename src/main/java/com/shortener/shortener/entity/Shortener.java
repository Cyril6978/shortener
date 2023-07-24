package com.shortener.shortener.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Shortener {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String shortId;
    private String realUrl;
    public Shortener(){}



    public Shortener(UUID id, String shortId, String realUrl) {
        this.id = id;
        this.shortId = shortId;
        this.realUrl = realUrl;
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
}