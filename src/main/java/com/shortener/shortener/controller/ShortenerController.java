package com.shortener.shortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shortener.shortener.dto.ShortenerDto;
import com.shortener.shortener.entity.Shortener;
import com.shortener.shortener.service.ShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

//@ControllerAdvice
@RestController
public class ShortenerController {

    @Autowired
    private ShortenerService shortenerService;
    @Value("${json.file.path}")
    private String filePath;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShortenerDto createUrl(@RequestBody Shortener shortener, HttpServletResponse response) throws IOException {

        if (!shortenerService.startWithHttpOrHttps(shortener.getRealUrl())) {
            // return new ShortenerDto();
            throw new RuntimeException("Erreur 400: invalid url");
            //HttpStatus s = HttpStatus.BAD_REQUEST.is4xxClientError();
            //throw new ResourceException(HttpStatus.BAD_REQUEST, "My message");
            //return HttpStatus.BAD_REQUEST;
        }
        shortener.setId(UUID.randomUUID());
        shortener.setShortId(shortenerService.generateShortId());
        shortener.setxRemovalToken(shortenerService.generateXRemovalToken());
        response.setHeader("generate x removal", shortener.getxRemovalToken());

        File file = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
        });
        for (int i = 0; i < myDataList.size(); i++) {
            if (myDataList.get(i).getShortId().equals(shortener.getShortId())) {
                shortener.setShortId(shortenerService.generateShortId());
                //i = -1;
            }
        }
        myDataList.add(shortener);
        objectMapper.writeValue(file, myDataList);
        return shortenerService.TransformShortenerEntityInShortenerDto(shortener);
    }

    @GetMapping("/{shortId}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortId) throws IOException {
        File file = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();

        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
        });
        String shortenerToDisplay = myDataList.stream().filter(
                myObj -> myObj.getShortId().equals(shortId)
        ).findFirst().get().getRealUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(shortenerToDisplay));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    @DeleteMapping("/links/{id}")
    public ResponseEntity<?> deleteShortener(@PathVariable UUID id, @RequestHeader("xRemovalToken") String removalToken) throws IOException {
        // response.getHeader()
        File file = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
        });
        if (myDataList.stream().filter(
                myObj -> myObj.getId().equals(id)).findFirst().isEmpty()) {
            return new ResponseEntity<>("Shortener is not find", HttpStatus.NOT_FOUND);
        }
        Shortener shortenerToDisplay = myDataList.stream().filter(
                myObj -> myObj.getId().equals(id)).findFirst().get();


        if (shortenerToDisplay.getxRemovalToken().equals(removalToken)) {
            myDataList.remove(shortenerToDisplay);
            objectMapper.writeValue(file, myDataList);

            return new ResponseEntity<>("Shortener deleted successfully", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>("Shortener is not deleted", HttpStatus.FORBIDDEN);
    }

}
