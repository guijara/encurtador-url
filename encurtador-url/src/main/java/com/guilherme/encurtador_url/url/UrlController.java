package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.url.dto.UrlRequestDto;
import com.guilherme.encurtador_url.url.dto.UrlResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @GetMapping()

    @PostMapping
    public ResponseEntity<UrlResponseDto> postUrl(@RequestBody UrlRequestDto dto){
        UrlResponseDto dtoResponse = urlService.encurtarUrl(dto.urlOriginal());
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }
}
