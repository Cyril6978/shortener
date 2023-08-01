package com.shortener.shortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shortener.shortener.entity.LogErrMessage;
import com.shortener.shortener.entity.Shortener;
import com.shortener.shortener.exception.InvalidUrlException;
import com.shortener.shortener.exception.ShortenerNotFoundException;
import com.shortener.shortener.service.ShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

//@ControllerAdvice
@RestController
public class ShortenerController {


    @Autowired
    private ShortenerService shortenerService;
    @Value("${json.file.path}")
    private String filePath;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    //@ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<?> createUrl(@RequestBody Shortener shortener, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            if (!shortenerService.startWithHttpOrHttpsOrWww(shortener.getRealUrl())) {

                throw new InvalidUrlException();
            }

            shortener.setId(UUID.randomUUID());
            shortener.setShortId(shortenerService.generateShortId());
            shortener.setxRemovalToken(shortenerService.generateXRemovalToken());
            shortener.setCreationDate(shortenerService.generateCreationDate());
            response.setHeader("generate x removal", shortener.getxRemovalToken());

            File file = new File(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
            });

            for (int i = 0; i < myDataList.size(); i++) {
                if (myDataList.get(i).getShortId().equals(shortener.getShortId())) {
                    shortener.setShortId(shortenerService.generateShortId());
                    i = -1;
                }
            }

            myDataList.add(shortener);
            objectMapper.writeValue(file, myDataList);
            return new ResponseEntity<>(shortenerService.TransformShortenerEntityInShortenerDto(shortener), HttpStatus.CREATED);
        } catch (InvalidUrlException e) {
            int lineNumber = e.getStackTrace()[0].getLineNumber();

            LogErrMessage error400 = new LogErrMessage();
            String ipAddress = request.getRemoteAddr();
            error400.setMethod("createUrl");
            error400.setPathHttp(shortener.getRealUrl());
            error400.setAdressIp(ipAddress);
            error400.setTypeOfError("Error 400");
            error400.setFileSrc("Shortener controller");
            error400.setLine(lineNumber);
            error400.setMessageError("L'adresse est invalide");

            shortenerService.generateErrorMessage(error400);

            return new ResponseEntity<>("invalid url", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{shortId}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortId) throws IOException {
        File file = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();

        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
        });
        Shortener shortenerToDisplay = myDataList.stream().filter(
                myObj -> myObj.getShortId().equals(shortId)
        ).findFirst().orElseThrow(() -> new ShortenerNotFoundException());


        shortenerToDisplay.setCreationDate(LocalDateTime.now());
        objectMapper.writeValue(file, myDataList);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(shortenerToDisplay.getRealUrl()));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    //tache planifiée
    @Scheduled(cron = "*/1 * * * * *") //rafraichissement toutes les minutes
    public void deleteExpiredShorteners() throws IOException {
        File file = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
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

    @DeleteMapping("/links/{id}")
    public ResponseEntity<?> deleteShortener(@PathVariable UUID id, @RequestHeader("xRemovalToken") String removalToken, HttpServletRequest request) throws IOException {
        // response.getHeader()
        File file = new File(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
        });
        if (myDataList.stream().filter(
                myObj -> myObj.getId().equals(id)).findFirst().isEmpty()) {

            LogErrMessage error404 = new LogErrMessage();

            error404.setMethod("deleteShortener");
            String ipAddress = request.getRemoteAddr();
            error404.setPathHttp(myDataList.stream().filter(
                    myObj -> myObj.getId().equals(id)).findFirst().get().getRealUrl());
            error404.setAdressIp(ipAddress);
            error404.setTypeOfError("Error 404");
            error404.setFileSrc("Shortener controller");
            error404.setLine(137);
            error404.setMessageError("Suppression impossible, ressource non trouvé.");

            shortenerService.generateErrorMessage(error404);

            return new ResponseEntity<>("Shortener is not find", HttpStatus.NOT_FOUND);
        }
        Shortener shortenerToDisplay = myDataList.stream().filter(
                myObj -> myObj.getId().equals(id)).findFirst().get();


        if (shortenerToDisplay.getxRemovalToken().equals(removalToken)) {
            myDataList.remove(shortenerToDisplay);
            objectMapper.writeValue(file, myDataList);

            return new ResponseEntity<>("Shortener deleted successfully", HttpStatus.NO_CONTENT);
        }

        LogErrMessage error403 = new LogErrMessage();

        error403.setMethod("deleteShortener");
        String ipAddress = request.getRemoteAddr();
        error403.setPathHttp(myDataList.stream().filter(
                myObj -> myObj.getId().equals(id)).findFirst().get().getRealUrl());
        error403.setAdressIp(ipAddress);
        error403.setTypeOfError("Error 403");
        error403.setFileSrc("Shortener controller");
        error403.setLine(158);
        error403.setMessageError("Token incorrect.");

        shortenerService.generateErrorMessage(error403);

        return new ResponseEntity<>("Shortener is not deleted", HttpStatus.FORBIDDEN);
    }

}