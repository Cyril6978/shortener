package com.shortener.shortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.shortener.entity.Shortener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
public class ShortenerControllerTest {

    @Autowired
    ShortenerController shortenerController;
    @Autowired
    private MockMvc mockMvc;
    @Value("${json.file.path}")
    private String filePath;

    private String shortenerTest = """
            [{
            "id" : "6815fe94-8b47-4420-8299-1669304b39ec",
            "shortId" : "AZERTYUI",
            "realUrl" : "https://www.url.com",
            "xRemovalToken" : "544fb8d4899b416a8a1fc7d2312d319b",
            "creationDate" : "2023-09-04T15:05:36.510556000"
            }]
            """;

    @Test
    public void shouldCreateShortener() throws Exception {

        String realUrl = "https://www.url.com";
        mockMvc.perform(post("/links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + realUrl + "\""))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.short-id").exists())
                .andExpect(jsonPath("$.real-url").exists())
                .andExpect(jsonPath("$.real-url").value(realUrl))
                .andExpect(header().exists("X-Removal-Token"))
                .andExpect(jsonPath("$.x-removal-token").doesNotExist());
    }

    @Test
    public void shouldRedirect() throws Exception {

        PrintWriter writer = new PrintWriter(filePath);
        writer.print(shortenerTest);
        writer.close();

        mockMvc.perform(get("/AZERTYUI"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("https://www.url.com"));

    }

    @Test
    public void shouldDeleteExpiredShortener() throws Exception {

        // Given
        String expiredShortenerTest = """
                [{
                "id" : "6815fe94-8b47-4420-8299-1669304b39ec",
                "shortId" : "AZERTYUI",
                "realUrl" : "https://www.url.com",
                "xRemovalToken" : "544fb8d4899b416a8a1fc7d2312d319b",
                "creationDate" : "2022-08-04T15:05:36.510556000"
                }]
                """;

        PrintWriter writer = new PrintWriter(filePath);
        writer.print(expiredShortenerTest);
        writer.close();

        // When
        shortenerController.deleteExpiredShorteners();

        // Then
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);
        List<Shortener> myDataList = objectMapper.readValue(file, new TypeReference<List<Shortener>>() {
        });
        assertTrue(myDataList.isEmpty());
    }

    @Test
    public void shouldDelete() throws Exception {

        PrintWriter writer = new PrintWriter(filePath);
        writer.print(shortenerTest);
        writer.close();


        String removalToken = "544fb8d4899b416a8a1fc7d2312d319b";
        String id = "6815fe94-8b47-4420-8299-1669304b39ec";


        mockMvc.perform(delete("/links/" + id)
                        .header("xRemovalToken", removalToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/links/" + id)
                        .header("xRemovalToken", removalToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldntDelete_invalidRemovalToken() throws Exception {

        // Given

        String id = "6815fe94-8b47-4420-8299-1669304b39ec";
        String invalidRemovalToken = "Not-A-VALID-t0ken";
        String creationDate = LocalDateTime.now().toString();

        String notExpiredShortenerTest = """
                [{
                "id" : "6815fe94-8b47-4420-8299-1669304b39ec",
                "shortId" : "AZERTYUI",
                "realUrl" : "https://www.url.com",
                "xRemovalToken" : "544fb8d4899b416a8a1fc7d2312d319b",
                "creationDate" : "%s"
                }]
                """.formatted(creationDate);

        PrintWriter writer = new PrintWriter(filePath);
        writer.print(notExpiredShortenerTest);
        writer.close();

        // When
        mockMvc.perform(delete("/links/" + id)
                        .header("xRemovalToken", invalidRemovalToken))

                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldntDelete_invalidId() throws Exception {

        // Given
        PrintWriter writer = new PrintWriter(filePath);
        writer.print(shortenerTest);
        writer.close();
        String invalidId = "6815fe94-8b47-4420-8299-1669304b39ea";
        String removalToken = "544fb8d4899b416a8a1fc7d2312d319b";

        // When
        mockMvc.perform(delete("/links/" + invalidId)
                        .header("xRemovalToken", removalToken))

                // Then
                .andExpect(status().isNotFound());
    }

}