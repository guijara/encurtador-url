package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.url.dto.UrlResponseCompleteDto;
import com.guilherme.encurtador_url.url.exception.*;
import com.guilherme.encurtador_url.user.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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

    //Valida a formatação da URL enviada pelo usuário
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

    //Valida conteúdo da URL enviada pelo usuário
    private void verificaConteudoUrl(String url){
        if(url == null || url.isBlank()){
            throw new UrlContentException("A url informada não deve ser vazia.");
        }
    }

    @Transactional
    public String encurtarUrl(String url, ExpirationType expirationType,UserEntity userLogado) {

        verificaConteudoUrl(url);
        verificaSeUrl(url);

        Optional<UrlEntity> urldb = urlRepository.findByOriginalUrl(url);

        if (!urldb.isPresent()){

            UrlEntity urlCriada = new UrlEntity(url);
            urlCriada.setUser(userLogado);

            if (expirationType.equals(ExpirationType.SEVEN_DAYS)){
                urlCriada.setExpiredAt(LocalDateTime.now().plusDays(7));
            }else if (expirationType.equals(ExpirationType.THREE_MONTHS)){
                urlCriada.setExpiredAt(LocalDateTime.now().plusMonths(3));
            }else{
                urlCriada.setExpiredAt(null);
            }

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
    public String recuperaUrlOriginal(String url){

        Long id = urlMath.decode(url);


        UrlEntity urlOriginal = urlRepository.findById(id).orElseThrow(() ->
                new UrlNonExistentException("A URL original não existe no banco de dados."));

        if (urlOriginal.getExpiredAt() != null && urlOriginal.getExpiredAt()
                .isBefore(LocalDateTime.now())){

            urlRepository.delete(urlOriginal);
            throw new UrlExpiredTimeException("A URL teve o tempo expirado.");
        }

        urlOriginal.setNumClicks(urlOriginal.getNumClicks()+1);

        return urlOriginal.getOriginalUrl();
    }

    @Transactional
    public void deletarUrl(Long id, UserEntity userLogado) {

        //valida url no banco
        UrlEntity url = urlRepository.findById(id).orElseThrow(() ->
                new UrlNonExistentException("A URL não existe no sistema"));

        //valida usuário
        if (!userLogado.equals(url.getUser())){
            throw new UserNotAllowedException("Você não tem permissão para deletar essa URL");
        }

        //deleta do banco
        urlRepository.delete(url);
    }

    public Page<UrlResponseCompleteDto> retornaUrlsPorUsuario(UserEntity user , Pageable pageable){

        //busca urls no banco baseado em um usuário
        Page<UrlEntity> page = urlRepository.findActiveByUser(user,LocalDateTime.now(),pageable);



        //converte as url entidades para dto
        return page.map(url -> new UrlResponseCompleteDto(
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getNumClicks(),
                url.getCreationAt()
        ));
    }

    //Verifica o tempo de expiração no banco baseado no cron e apaga
    @Scheduled(fixedRate = 60000)
//    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteExpiredUrls(){
        urlRepository.deleteByExpiredAtBefore(LocalDateTime.now());
        System.out.println("Cleanup was a success: expired URLs were deleted");
    }
}
