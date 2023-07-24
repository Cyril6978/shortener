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
import java.util.List;
import java.util.UUID;

@RestController
public class ShortenerController {

@Autowired

    ShortenerRepository shortenerRepository;

@PostMapping("")
public Shortener createUrl(@RequestBody Shortener shortener) throws IOException {
     //shortenerRepository.save(shortener);
    shortener.setId(UUID.randomUUID());
   // shortener.setShortId(shortener.getId().);

    File file = new File("src/main/resources/links.json");
    ObjectMapper objectMapper = new ObjectMapper();
    List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {});
    myDataList.add(shortener);
    objectMapper.writeValue(file, myDataList);

    return shortener;
}

}
