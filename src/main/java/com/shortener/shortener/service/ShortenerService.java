package com.shortener.shortener.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shortener.shortener.controller.LoggingController;
import com.shortener.shortener.dto.ShortenerDto;
import com.shortener.shortener.entity.Error;
import com.shortener.shortener.entity.Shortener;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ShortenerService {

    Logger logger = LoggerFactory.getLogger(ShortenerService.class);
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
//    Base64.Encoder encoder = Base64.getUrlEncoder();
//    SecureRandom random = new SecureRandom();
//    byte[] array = new byte[6]; // length is bounded 8
//    random.nextBytes(array);
//    return encoder.encodeToString(array);
    }

    public Boolean startWithHttpOrHttpsOrWww(String realUrl) {
        if (realUrl.substring(0, 4).equals("http") && realUrl.substring(7, 10).equals("www") || realUrl.substring(8, 11).equals("www")) {
            return true;
        }
       return false;
    }

    public ShortenerDto TransformShortenerEntityInShortenerDto(Shortener shortener) {
        ShortenerDto shortenerDto = new ShortenerDto();
        shortenerDto.setId(shortener.getId());
        shortenerDto.setShortId(shortener.getShortId());
        shortenerDto.setRealUrl(shortener.getRealUrl());
        // Récupere et ajoute au DTO toutes les propriétés que tu auras déclarées des 2 côtés
        return shortenerDto;
    }

    public String generateXRemovalToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String generateCreationDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        return now.format(formatter);
    }

    // Méthode pour charger les données depuis le fichier links.json
    public void generateErrorMessage(Error error) {
        //méthode> <chemin HTTP> from <addresse IP source>, <type de l'erreur>: <message d'erreur> (<fichier source> => <ligne de code>)

        logger.error(error.getMethod() + " " + error.getPathHttp() + " from " + error.getAdressIp() + ", "
                + error.getTypeOfError() + ":" + error.getMessageError() + " (" + error.getFileSrc() + " => " + error.getLine() + ")");

    }

}
