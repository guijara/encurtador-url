package com.guilherme.encurtador_url.url;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls")
@Data
public class UrlEntity {

    public UrlEntity() {

    }

    public UrlEntity(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    @SequenceGenerator(initialValue = 100000)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalUrl;

    @Column(unique = true)
    private String shortUrl;

    private LocalDateTime creationAt;
}
