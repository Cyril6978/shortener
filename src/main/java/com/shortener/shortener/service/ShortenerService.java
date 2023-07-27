package com.shortener.shortener.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shortener.shortener.dto.ShortenerDto;
import com.shortener.shortener.entity.Shortener;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class ShortenerService {

    public Boolean startWithHttpOrHttps(String realUrl) {
        if (realUrl.substring(0, 4).equals("http")) {
            return true;
        }
        return false;
    }

    public ShortenerDto TransformShortenerEntityInShortenerDto(Shortener shortener) {
        ShortenerDto shortenerDto = new ShortenerDto();
        shortenerDto.setId(shortener.getId());
        shortenerDto.setShortId(shortener.getShortId());
        shortenerDto.setRealUrl(shortener.getRealUrl());
        // Récupère et ajoute au DTO toutes les propriétés que tu auras déclarées des 2 côtés
        return shortenerDto;
    }

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generateShortId() {
        String str = "";
        SecureRandom rand = new SecureRandom();
        int longueur = ALPHABET.length();
        for (int i = 0; i < 8; i++) {
            int k = rand.nextInt(longueur);
            str = str + ALPHABET.charAt(k);
        }
        return str;
    }

    public String generateXRemovalToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private List<Shortener> shortenerList = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadShortenersFromFile();
    }

    // Méthode pour charger les données depuis le fichier links.json
    public void loadShortenersFromFile() {
        File file = new File("src/main/resources/links.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<>() {
            });
            shortenerList.addAll(myDataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeShortenersToFile() {
        File file = new File("src/main/resources/links.json");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(file, shortenerList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Shortener findById(UUID id) {
        for (Shortener shortener : shortenerList) {
            if (shortener.getId().equals(id)) {
                return shortener;
            }
        }
        return null;
    }

    public void deleteShortener(UUID id) {
        Shortener shortenerToDelete = null;
        for (Shortener shortener : shortenerList) {
            if (shortener.getId().equals(id)) {
                shortenerToDelete = shortener;
                break;
            }
        }
        if (shortenerToDelete != null) {
            shortenerList.remove(shortenerToDelete);
            writeShortenersToFile(); // Sauvegarder les modifications dans le fichier
        }
    }
}
