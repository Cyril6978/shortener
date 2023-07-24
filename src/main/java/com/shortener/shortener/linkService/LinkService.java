package com.shortener.shortener.linkService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.shortener.entity.Shortener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    private static final String FILE_PATH = "src/main/resources/links.json";

    private List<Shortener> links;

    public LinkService() {
        this.links = loadFromFile();
    }

    public List<Shortener> getAllLinks() {
        return links;
    }

    public void addLink(Shortener shortener) {
        // Générer manuellement l'ID pour l'objet Shortener
        shortener.setId(UUID.randomUUID());
        links.add(shortener);
        saveToFile();
    }

    // Utiliser Jackson pour lire les données depuis le fichier JSON et les charger dans la liste
    private List<Shortener> loadFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(FILE_PATH), new TypeReference<List<Shortener>>() {
            });
        } catch (IOException e) {
            return new ArrayList<>(); // Si le fichier n'existe pas ou est vide, retourner une liste vide
        }
    }

    // Utiliser Jackson pour écrire les données de la liste dans le fichier JSON
    private void saveToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(FILE_PATH), links);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur d'écriture du fichier ici si nécessaire
        }
    }
}
