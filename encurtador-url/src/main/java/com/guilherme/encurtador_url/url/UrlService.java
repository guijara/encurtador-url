package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.url.exception.UrlConteudoException;
import com.guilherme.encurtador_url.url.exception.UrlFormatException;
import com.guilherme.encurtador_url.url.exception.UrlNãoExistenteException;
import com.guilherme.encurtador_url.user.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlService {

    private UrlRepository urlRepository;
    private UrlMath urlMath;

    public UrlService(UrlRepository urlRepository, UrlMath urlMath){
        this.urlRepository = urlRepository;
        this.urlMath = urlMath;
    }

    private void verificaSeUrl(String url){

        try{
            URL url1 = new URL(url);
            url1.toURI();
        }catch (MalformedURLException exception){
            throw new UrlFormatException("A string não é uma URL válida.");
        } catch (URISyntaxException e) {
            throw new UrlFormatException("A string não é uma URL válida.");
        }
    }

    private void verificaConteudoUrl(String url){
        if(url == null || url.isBlank()){
            throw new UrlConteudoException("A url informada não deve ser vazia.");
        }
    }

    @Transactional
    public String encurtarUrl(String url, UserEntity userLogado) {

        verificaConteudoUrl(url);
        verificaSeUrl(url);

        Optional<UrlEntity> urldb = urlRepository.findByOriginalUrl(url);

        if (!urldb.isPresent()){

            UrlEntity urlCriada = new UrlEntity(url);
            urlCriada.setUser(userLogado);

            //prevenção de Race Condition por duas Threads simultâneas alterando o valor no banco de dados.
            //Garantindo Idempotência das requisições

            try{

                //flush força o erro a aparecer instantaneamente

                urlRepository.saveAndFlush(urlCriada);

                Long id = urlCriada.getId();
                String shortUrl = urlMath.encode(id);
                urlCriada.setShortUrl(shortUrl);
                urlCriada.setCreationAt(LocalDateTime.now());

                //esse save é apenas para deixar explicito o salvamento, pois, o @Transactional já garante que ao alterar
                //o objeto, o Hibernate faça o Dirty Checking e veja que deve enviar um UPDATE para o banco
                //

                urlRepository.save(urlCriada);

                return shortUrl;
            }catch (DataIntegrityViolationException exception){

                //fazemos a busca novamente para pegar o elemento inserido pela primeira thread

                Optional<UrlEntity> urldbAux = urlRepository.findByOriginalUrl(url);
                String shortUrl = urldbAux.get().getShortUrl();
                return shortUrl;
            }

        }else{

            String shortUrl = urldb.get().getShortUrl();
            return shortUrl;
        }
    }

//    @Transactional
//    public String encurtarUrl(String url) {
//
//        verificaConteudoUrl(url);
//        verificaSeUrl(url);
//
//        Optional<UrlEntity> urldb = urlRepository.findByOriginalUrl(url);
//
//        if (!urldb.isPresent()){
//
//            UrlEntity urlCriada = new UrlEntity(url);
//            urlRepository.save(urlCriada);
//            Long id = urlCriada.getId();
//            String shortUrl = urlMath.encode(id);
//            urlCriada.setShortUrl(shortUrl);
//            return shortUrl;
//
//        }else{
//
//            String shortUrl = urldb.get().getShortUrl();
//            return shortUrl;
//        }
//    }

    @Transactional
    public String retornaOriginal(String url){

        Long id = urlMath.decode(url);


        UrlEntity urlOriginal = urlRepository.findById(id).orElseThrow(() -> new UrlNãoExistenteException("A url original não existe no banco de dados."));

        urlOriginal.setNumClicks(urlOriginal.getNumClicks()+1);

        return urlOriginal.getOriginalUrl();
    }
}
