package com.guilherme.encurtador_url.config.security;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {


    // ConcurrentHashMap é utilizado para lidar com Threads simultâneas enviadas pleo usuário
    // Nesse caso, o Map é a "Cache" que guarda os estados de cada bucket do usuário
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Pipeline de validação de requisições no bucket do usuário
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String key = getIdentifier(request);

        // Procura o bucket do usuário (pela key buscada),
        // caso não exista, criamos um bucket novo para ele e armazenamos no map
        Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());

        // Consome uma requisição do bucket
        if (bucket.tryConsume(1)) {

            //Caso o bucket ainda tenha requisições, ele apenas direciona o usuário pro controller
            filterChain.doFilter(request, response);
        } else {

            // Caso não, ele lança o erro
            sendErrorResponse(response);
        }
    }

    private String getIdentifier(HttpServletRequest request) {

        // Coleta o usuário da memória de contexto
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }

        // Caso não tenha usuário na memória da Thread, ele retorna o IP.
        return request.getRemoteAddr();
    }

    // Cria novo bucket caso o usuário ainda não possua
    private Bucket createNewBucket() {
        // Define o limite de 5 requisições por minuto
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(5).refillGreedy(5, Duration.ofMinutes(1)))
                .build();
    }

    // Lança o erro
    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(429); // Too Many Requests
        response.setContentType("application/json");
        response.getWriter().write("{ \"erro\": \"Limite de requisições excedido. Tente novamente em 1 minuto.\" }");
    }
}
