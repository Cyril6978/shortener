package com.shortener.shortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shortener.shortener.dto.ShortenerDto;
import com.shortener.shortener.entity.Shortener;
import com.shortener.shortener.service.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
public class ShortenerController {

@Autowired
private ShortenerService shortenerService;

@PostMapping("")
public ShortenerDto  createUrl(@RequestBody Shortener shortener) throws IOException {
    if(!shortenerService.startWithHttpOrHttps(shortener.getRealUrl())){
        return new ShortenerDto();
    }
    shortener.setId(UUID.randomUUID());
    shortener.setShortId(shortenerService.generateShortId());
    shortener.setxRemovalToken("Coucou");
    File file = new File("src/main/resources/links.json");
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {});
    for(int i = 0; i < myDataList.size(); i++){
        if(myDataList.get(i).getShortId().equals(shortener.getShortId())){
            shortener.setShortId(shortenerService.generateShortId());
            //i = -1;
        }
    }
    myDataList.add(shortener);
    objectMapper.writeValue(file, myDataList);
    return shortenerService.TransformShortenerEntityInShortenerDto(shortener);

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
/*    @GetMapping("/{shortId}")
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
    }*/

}
