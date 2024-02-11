package com.example.springs3.repository;

import com.example.springs3.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3Repository extends JpaRepository<Image, Long> {
}
