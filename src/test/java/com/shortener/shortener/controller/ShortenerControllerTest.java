package com.shortener.shortener.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.PrintWriter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
public class ShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Value("${json.file.path}")
    private String filePath;

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
        // given
        String shortenerTest = """
                [{
                "id" : "6815fe94-8b47-4420-8299-1669304b39ec",
                "shortId" : "AZERTYUI",
                "realUrl" : "https://www.url.com",
                "xRemovalToken" : "544fb8d4899b416a8a1fc7d2312d319b",
                "creationDate" : "2023-08-04T15:05:36.510556"
                }]
                """;

//        File file = new File(filePath);
//        ObjectMapper om = new ObjectMapper();
//        om.writeValue(file, shortenerTest);

        PrintWriter writer = new PrintWriter(filePath);
        writer.print(shortenerTest);
        writer.close();

        mockMvc.perform(get("/AZERTYUI"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("https://www.url.com"));

    }
}