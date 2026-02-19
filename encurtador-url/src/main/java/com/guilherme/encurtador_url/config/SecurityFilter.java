package com.guilherme.encurtador_url.config;

import com.guilherme.encurtador_url.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenConfig tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenConfig tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        //  Pega o token do cabeçalho
        var token = this.recoverToken(request);

        //  Se o token existir, validamos
        if(token != null){
            // Valida e pega o login (subject) de dentro do token
            var login = tokenService.validateToken(token);

            if(!login.isEmpty()){
                // Busca o usuário no banco
                UserDetails user = userRepository.findByUsername(login).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                // Cria a autenticação do Spring
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Salva no contexto (Agora o Spring sabe quem está logado para essa requisição)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Passa para o próximo filtro (Controller)
        try{
            filterChain.doFilter(request, response);
        }catch (ServletException exception){
            throw new ServletException("");
        }
    }

    // Metodo auxiliar para limpar o "Bearer " do cabeçalho
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
