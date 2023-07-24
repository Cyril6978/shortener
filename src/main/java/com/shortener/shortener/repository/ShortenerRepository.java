package com.shortener.shortener.repository;

import com.shortener.shortener.entity.Shortener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShortenerRepository extends JpaRepository<Shortener, UUID> {

}
