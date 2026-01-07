package com.guilherme.encurtador_url.url;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity,Long> {

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);
}
