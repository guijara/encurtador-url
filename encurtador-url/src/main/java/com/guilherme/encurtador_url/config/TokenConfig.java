package com.guilherme.encurtador_url.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.guilherme.encurtador_url.user.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenConfig {

    @Value("${api.security.token.secret}")
    private String secret;


    // Gera o token
    public String generateToken(UserEntity user){
        try{
            // Algoritmo de Criptografia
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("encurtador-url-api") // Quem emitiu
                    .withSubject(user.getUsername())  // Quem é o dono do token
                    .withExpiresAt(genExpirationDate()) // Validade
                    .sign(algorithm); // Assinatura digital

            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }


    // Valida tokens para requisições
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("encurtador-url-api")
                    .build()
                    .verify(token) // Se o token for inválido ou expirado, lança exceção aqui
                    .getSubject(); // Retorna o username que estava escondido no token
        } catch (JWTVerificationException exception){
            // Retorna vazio se o token for inválido, para o Spring bloquear o acesso
            return "";
        }
    }

    // Define que o token expira em 2 horas (fuso horário de Cuiabá -04:00)
    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-04:00"));
    }
}
