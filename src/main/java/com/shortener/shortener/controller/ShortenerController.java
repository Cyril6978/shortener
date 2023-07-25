package com.shortener.shortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.shortener.entity.Shortener;
import com.shortener.shortener.service.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;


import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
public class ShortenerController {

@Autowired
private ShortenerService shortenerService;

@PostMapping("")
public Shortener createUrl(@RequestBody Shortener shortener) throws IOException {
    shortener.setId(UUID.randomUUID());
    shortener.setShortId(shortenerService.generateShortId());
    File file = new File("src/main/resources/links.json");
    ObjectMapper objectMapper = new ObjectMapper();
    List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {});
    for(int i = 0; i < myDataList.size(); i++){
        if(myDataList.get(i).getShortId()==shortener.getShortId()){
            shortener.setShortId(shortenerService.generateShortId());
            i = -1;
        }
    }
    myDataList.add(shortener);
    objectMapper.writeValue(file, myDataList);
    return shortener;
}
@GetMapping("/{shortId}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortId) throws IOException{
    File file = new File("src/main/resources/links.json");
    ObjectMapper objectMapper = new ObjectMapper();
    List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {});
    String shortenerToDisplay =  myDataList.stream().filter(
            myObj -> myObj.getShortId().equals(shortId)
    ).findFirst().get().getRealUrl();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(shortenerToDisplay));

return new ResponseEntity<>(headers,HttpStatus.FOUND);
}

}
