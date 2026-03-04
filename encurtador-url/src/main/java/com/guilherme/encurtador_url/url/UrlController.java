package com.guilherme.encurtador_url.url;

import com.guilherme.encurtador_url.url.dto.UrlRequestDto;
import com.guilherme.encurtador_url.url.dto.UrlResponseCompleteDto;
import com.guilherme.encurtador_url.url.dto.UrlResponseDto;
import com.guilherme.encurtador_url.user.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Void> getUrl(
            @Parameter(description = "URL encurtada",example = "Q0u")
            @PathVariable String shortUrl){
        String urlOriginal = urlService.retornaOriginal(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlOriginal)).build();
    }


    @Operation(summary = "Encurtar URL", description = "Recebe a URL original longa e retorna uma URL encurtada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "O processo deu certo e a URL encurtada foi criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "O processo deu erro por input inválido da URL original."),
            @ApiResponse(responseCode = "410", description = "O processo deu erro porquê a URL está expirada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponseDto> postUrl(@RequestBody UrlRequestDto dto,
                                          @AuthenticationPrincipal UserEntity userLogado){
        String shortUrl = urlService.encurtarUrl(dto.url(),dto.expirationType(),userLogado);
        String urlResponse = url + shortUrl;
        UrlResponseDto dtoResponse = new UrlResponseDto(urlResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }

    @Operation(summary = "Deletar URL", description = "Recebe um ID de URL e deleta do banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "O usuário que tentou realizar essa atividade não possui permissão"),
            @ApiResponse(responseCode = "204", description = "Url foi deletada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/api/urls/{id}")
    public ResponseEntity<Void> deleteUrl(
            @Parameter(description = "ID da URL que vai ser deletada", example = "200")
            @PathVariable Long id, @AuthenticationPrincipal UserEntity userLogado){
        urlService.deletarUrl(id,userLogado);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Listar URLs", description = "Recebe um usuário e lista todas as URLs pertencentes a ele")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a página da lista de URLs do usuário"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/api/urls")
    public ResponseEntity<Page<UrlResponseCompleteDto>> showUrls(
            @AuthenticationPrincipal UserEntity user,
            @Parameter(description = "Configurações de paginação",
            example = "{\"page\": 0, \"size\": 10}")
            @PageableDefault(sort = "creationAt", direction = Sort.Direction.DESC,
            size = 10) Pageable pageable){
        Page<UrlResponseCompleteDto> page = urlService.retornaUrlsPorUsuario(user,pageable);
        return ResponseEntity.ok(page);
    }

}
