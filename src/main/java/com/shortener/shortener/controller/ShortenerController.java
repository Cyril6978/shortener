package com.shortener.shortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.shortener.entity.Shortener;
import com.shortener.shortener.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
public class ShortenerController {

@Autowired

    ShortenerRepository shortenerRepository;

@PostMapping("")
public Shortener createUrl(@RequestBody Shortener shortener) throws IOException {


    shortener.setId(UUID.randomUUID());
    shortener.setShortId(generateShortId());

    File file = new File("src/main/resources/links.json");
    ObjectMapper objectMapper = new ObjectMapper();
    List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {});

    for(int i = 0; i < myDataList.size(); i++){
        if(myDataList.get(i).getShortId()==shortener.getShortId()){
            shortener.setShortId(generateShortId());
            i = -1;
        }
    }
    myDataList.add(shortener);
    objectMapper.writeValue(file, myDataList);

    return shortener;
}

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
