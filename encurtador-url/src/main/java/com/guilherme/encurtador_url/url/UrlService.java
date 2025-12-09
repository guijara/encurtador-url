package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.url.exception.UrlInputException;
import com.guilherme.encurtador_url.url.exception.UrlNãoExistenteException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        if(url.isBlank()){
            throw new UrlInputException("A url informada não deve ser vazia.");
        }
        UrlEntity urlCriada = new UrlEntity(url);

        urlRepository.save(urlCriada);

        Long id = urlCriada.getId();
        String shortUrl = urlMath.encode(id);
        urlCriada.setShortUrl(shortUrl);

        return shortUrl;
    }

    public String retornaOriginal(String url){
        Long id = urlMath.decode(url);


        UrlEntity urlOriginal = urlRepository.findById(id).orElseThrow(() -> new UrlNãoExistenteException("A url original não existe no banco de dados."));

        return urlOriginal.getOriginalUrl();
    }
}
