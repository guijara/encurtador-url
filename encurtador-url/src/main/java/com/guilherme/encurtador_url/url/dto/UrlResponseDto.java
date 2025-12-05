package com.guilherme.encurtador_url.url.dto;

public record UrlResponseDto(String shortUrl) {

    public UrlResponseDto(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
