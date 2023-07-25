package com.shortener.shortener.service;

import com.shortener.shortener.dto.ShortenerDto;
import com.shortener.shortener.entity.Shortener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Service
public class ShortenerService {

    public Boolean startWithHttpOrHttps(String realUrl){
        if(realUrl.substring(0,4).equals("http")) {
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

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generateShortId(){
        String str="";
        SecureRandom rand = new SecureRandom();
        int longueur = ALPHABET.length();
        for(int i = 0; i < 8; i++) {
            int k = rand.nextInt(longueur);
            str = str + ALPHABET.charAt(k);
        }
        return str;
    }

    public String generateXRemovalToken(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }


}
