package com.shortener.shortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shortener.shortener.dto.ShortenerDto;
import com.shortener.shortener.entity.Shortener;
import com.shortener.shortener.service.ShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
public class ShortenerController {

    @Autowired
    private ShortenerService shortenerService;

    @PostMapping("")
    public ShortenerDto createUrl(@RequestBody Shortener shortener, HttpServletResponse response) throws IOException {
        if (!shortenerService.startWithHttpOrHttps(shortener.getRealUrl())) {
            return new ShortenerDto();
        }

        shortener.setId(UUID.randomUUID());
        shortener.setShortId(shortenerService.generateShortId());
        shortener.setxRemovalToken(shortenerService.generateXRemovalToken());
        shortener.setCreationDate(shortenerService.generateCreationDate());
        response.setHeader("generate x removal token", shortener.getxRemovalToken());
        File file = new File("src/main/resources/links.json");
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

    // tâche planifiée
    @Scheduled(cron = "${cron}") //rafraichissement toutes les minutes
    public void deleteExpiredShorteners() throws IOException {
        File file = new File("src/main/resources/links.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
        });


        // Supprimer les shorteners expirés
        myDataList.removeIf(shortener -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            LocalDateTime creationDate = LocalDateTime.parse(shortener.getCreationDate(), formatter);
            return creationDate.plusDays(30).isBefore(LocalDateTime.now());
        });

        // maj du fichier
        objectMapper.writeValue(file, myDataList);

    }


    @GetMapping("/{shortId}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortId) throws IOException {
        File file = new File("src/main/resources/links.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
        });
        Shortener shortenerToDisplay = myDataList.stream().filter(
                myObj -> myObj.getShortId().equals(shortId)
        ).findFirst().get();


        shortenerToDisplay.setCreationDate(LocalDateTime.now());
        objectMapper.writeValue(file, myDataList);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(shortenerToDisplay.getRealUrl()));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);


    }

    @DeleteMapping("/{shortId}")
    public ResponseEntity<String> deleteShortener(@PathVariable String shortId) throws IOException {
        File file = new File("src/main/resources/links.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
        });

        // Recherche du shortener à supprimer en fonction de son ID
        Shortener shortenerToDelete = myDataList.stream()
                .filter(myObj -> myObj.getShortId().equals(shortId))
                .findFirst()
                .orElse(null);

        if (shortenerToDelete != null) {
            myDataList.remove(shortenerToDelete);

            // Écrire la liste mise à jour dans le fichier
            objectMapper.writeValue(file, myDataList);

            return ResponseEntity.ok("Shortener supprimé avec succès !");
        } else {
            return ResponseEntity.notFound().build();
        }
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
