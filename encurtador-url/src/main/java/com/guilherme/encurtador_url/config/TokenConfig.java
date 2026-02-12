package com.guilherme.encurtador_url.config;

public class TokenConfig {

    @Value("${api.security.token.secret}")
    private String secret;

    // 1. GERAR TOKEN (Login)
    public String generateToken(UserEntity user){
        try{
            // Algoritmo de Criptografia HMAC256
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("encurtador-url-api") // Quem emitiu (Nome da sua API)
                    .withSubject(user.getUsername())  // Quem é o dono do token (ID ou Username)
                    .withExpiresAt(genExpirationDate()) // Validade
                    .sign(algorithm); // Assina digitalmente

            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // 2. VALIDAR TOKEN (Filtro de Segurança)
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
