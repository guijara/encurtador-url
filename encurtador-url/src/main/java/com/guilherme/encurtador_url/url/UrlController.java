package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.url.dto.UrlRequestDto;
import com.guilherme.encurtador_url.url.dto.UrlResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Tag(name = "URL",description = "Criação e Visualização das URLs")
public class UrlController {

    @Value("${api.host.base-url}")
    private String url;
    private UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Operation(summary = "Retorna URL original", description = "Recebe a URL encurtada e devolve a original longa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "O processo deu certo e o link foi redirecionado corretamente"),
            @ApiResponse(responseCode = "404", description = "O processo deu erro por URL não encontrada no banco de dados."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> getUrl(@PathVariable String shortUrl){
        String urlOriginal = urlService.retornaOriginal(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlOriginal)).build();
    }


    @Operation(summary = "Encurtar URL", description = "Recebe a URL original longa e retorna uma URL encurtada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "O processo deu certo e a URL encurtada foi criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "O processo deu erro por input inválido da URL original."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponseDto> postUrl(@RequestBody UrlRequestDto dto){
        String shortUrl = urlService.encurtarUrl(dto.url());
        String urlResponse = url + shortUrl;
        UrlResponseDto dtoResponse = new UrlResponseDto(urlResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }
}
