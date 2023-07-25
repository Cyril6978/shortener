package com.shortener.shortener.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
@Service
public class ShortenerService {

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
}
