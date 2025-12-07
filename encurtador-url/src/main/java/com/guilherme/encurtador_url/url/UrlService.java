package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.url.dto.UrlResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlService {

    private UrlRepository urlRepository;
    private UrlMath urlMath;

    public UrlService(UrlRepository urlRepository, UrlMath urlMath){
        this.urlRepository = urlRepository;
        this.urlMath = urlMath;
    }

    @Transactional
    public String encurtarUrl(String url) {
        UrlEntity urlCriada = new UrlEntity(url);

        urlRepository.save(urlCriada);

        Long id = urlCriada.getId();
        String shortUrl = urlMath.encode(id);
        urlCriada.setShortUrl(shortUrl);

        return shortUrl;
    }
}
