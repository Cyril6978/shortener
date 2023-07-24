package com.shortener.shortener.controller;

import com.shortener.shortener.entity.Shortener;
import com.shortener.shortener.linkService.LinkService;
import com.shortener.shortener.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
