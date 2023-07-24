package com.shortener.shortener.controller;

import com.shortener.shortener.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortenerController {

@Autowired
    ShortenerRepository shortenerRepository;




}
