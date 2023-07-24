package com.shortener.shortener.repository;

import com.shortener.shortener.entity.Shortener;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
//@org.springframework.context.annotation.Configuration
//@EnableSpringDataWebSupport

@Repository
//@RepositoryRestResource(path ="/links")
public interface ShortenerRepository extends JpaRepository<Shortener, UUID> {



}
