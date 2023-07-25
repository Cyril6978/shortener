package com.shortener.shortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.shortener.entity.Shortener;
import com.shortener.shortener.linkService.LinkService;
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

@RestController
public class ShortenerController {

    @Autowired
    ShortenerRepository shortenerRepository;

    @Autowired
    LinkService linkService;

    @PostMapping(" ")
    public Shortener createUrl(@RequestBody Shortener shortener) {
        linkService.addLink(shortener);
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
