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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(initialValue = 100000, allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String originalUrl;

    @Column(unique = true)
    private String shortUrl;

    @Column(nullable = false)
    private Integer numClicks;

    private LocalDateTime creationAt;
}
