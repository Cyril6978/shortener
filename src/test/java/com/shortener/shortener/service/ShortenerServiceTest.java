package com.shortener.shortener.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class ShortenerServiceTest {

    @InjectMocks
    private ShortenerService shortenerService;

    @Test
    public void shouldGenerateShortId() {
        // given

        // when
        String shortId = shortenerService.generateShortId();
        // then
        assertNotNull(shortId);
        assertEquals(8, shortId.length());
        List<Character> forbiddenChars = shortId.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> !ShortenerService.ALPHABET.contains(c.toString()))
                .toList();
        assertTrue(forbiddenChars.isEmpty());
    }

}
