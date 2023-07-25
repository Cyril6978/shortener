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
import java.security.SecureRandom;
import java.util.Base64;
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



        Base64.Encoder encoder = Base64.getUrlEncoder();
        SecureRandom random = new SecureRandom();


        public String tiny1() {
            byte[] array = new byte[6]; // length is bounded 8
            random.nextBytes(array);
            return encoder.encodeToString(array);



    }
    @GetMapping("/{shortId}")
    public ResponseEntity<String> redirectToRealUrl(@PathVariable String shortId) throws IOException {
        File file = new File("src/main/resources/links.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {});

        for (Shortener link : myDataList) {
            if (link.getShortId().equals(shortId)) {z
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(link.getRealUrl()));
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
