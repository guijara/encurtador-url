package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.url.dto.UrlRequestDto;
import com.guilherme.encurtador_url.url.dto.UrlResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {

    private UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> getUrl(@PathVariable String shortUrl){
        String urlOriginal = urlService.retornaOriginal(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlOriginal)).build();
    }

    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponseDto> postUrl(@RequestBody UrlRequestDto dto){
        String shortUrl = urlService.encurtarUrl(dto.url());
        String urlResponse = "http://localhost:8080/" + shortUrl;
        UrlResponseDto dtoResponse = new UrlResponseDto(urlResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }
}
