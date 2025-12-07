package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.url.dto.UrlRequestDto;
import com.guilherme.encurtador_url.url.dto.UrlResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @GetMapping()

    @PostMapping
    public ResponseEntity<UrlResponseDto> postUrl(@RequestBody UrlRequestDto dto){
        String shortUrl = urlService.encurtarUrl(dto.urlOriginal());
        String urlResponse = "http://localhost:8080/" + shortUrl;
        UrlResponseDto dtoResponse = new UrlResponseDto(urlResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }
}
