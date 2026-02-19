package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.user.UserEntity;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(initialValue = 100000, allocationSize = 1)
    private Long id;

    @Column(name = "original_url", nullable = false, unique = true)
    private String originalUrl;

    @Column(name = "short_url", unique = true)
    private String shortUrl;

    @Column(name = "num_clicks", nullable = false)
    private Integer numClicks = 0;

    @Column(name = "creation_date")
    private LocalDateTime creationAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
