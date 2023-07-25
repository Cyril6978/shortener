package com.shortener.shortener.service;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class ShortenerService {

    public String generateShortId(){
        String str="";
        Random rand = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int longueur = alphabet.length();
        for(int i = 0; i < 8; i++) {
            int k = rand.nextInt(longueur);
            str = str + alphabet.charAt(k);
        }
        return str;
//    Base64.Encoder encoder = Base64.getUrlEncoder();
//    SecureRandom random = new SecureRandom();
//    byte[] array = new byte[6]; // length is bounded 8
//    random.nextBytes(array);
//    return encoder.encodeToString(array);


    }
}
