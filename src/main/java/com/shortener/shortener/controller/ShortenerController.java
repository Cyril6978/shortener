package com.shortener.shortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.shortener.entity.Shortener;
import com.shortener.shortener.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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
    @GetMapping("/{shortId}")
    public ResponseEntity<String> redirectToRealUrl(@PathVariable String shortId) throws IOException {
        File file = new File("src/main/resources/links.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {});

        for (Shortener link : myDataList) {
            if (link.getShortId().equals(shortId)) {
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(link.getRealUrl()));
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            }
        }
        return ResponseEntity.notFound().build();
    }


}
