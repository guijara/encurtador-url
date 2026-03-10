# Encurtador de URL

Um ecossistema completo para encurtamento, gestão e análise de URLs. Desenvolvido com uma arquitetura desacoplada utilizando Spring Boot 4 no backend e Next.js 15 no frontend, garantindo alta performance, segurança e uma experiência de usuário fluida.


## Tecnologias Utilizadas

### Backend
* **Java 21** - Linguagem base.
* **Spring Boot 4** - Framework para criação da API.
* **Spring Security & JWT** - Autenticação e autorização via tokens.
* **PostgreSQL** - Banco de dados relacional robusto.
* **Docker & Docker Compose** - Para containerização e orquestração do ambiente.
* **Flyway** - Para migração e versionamento do banco de dados.
* **Swagger (OpenAPI)** - Documentação interativa da API.
* **Rate Limiting (Bucket4j)** - Proteção contra ataques de força bruta e spam, limitando requisições por IP.

### Frontend
* **Next.js 15 (App Router)** - Framework React moderno com renderização otimizada.
* **TypeScript** - Tipagem estática para evitar erros em tempo de execução e facilitar a manutenção.
* **Tailwind CSS** - Estilização utilitária para uma interface responsiva, moderna e em Dark Mode.
* **Lucide React** - Conjunto de ícones minimalistas e elegantes.
* **Axios** - Cliente HTTP configurado com interceptadores para gestão de tokens JWT.


## Funcionalides do sistema
* **Sistema de Usuários** - Registro e login com criptografia BCrypt.
* **Expiração Inteligente** - Escolha entre links Permanentes, de 7 dias ou 3 meses (Gerenciado via Enums no Java).
* **Dashboard de Analytics** - Acompanhamento do número de cliques e estatísticas de uso.
* **Segurança de Dados** - Somente o criador da URL pode visualizar estatísticas ou remover o link.
* **Interface Responsiva** - Dashboard adaptável para dispositivos móveis e desktop.


## Pré-requisitos

Para rodar este projeto, precisas apenas ter instalado:
* [Docker](https://www.docker.com/) e Docker Compose.
* (Opcional) Node.js 20+ e Java 21.

## Como Rodar (Passo a Passo)

A forma mais simples é utilizando o Docker, que configura automaticamente o Banco de Dados e a Aplicação.

1.  **Clone o repositório** (ou baixe os arquivos):
    ```bash
    git clone <https://github.com/guijara/encurtador-url.git>
    ```

2.  **Inicie o ambiente:**
    Na raiz do projeto, execute:
    ```bash
    docker-compose up -d db --build
    ```
    *Aguarde até ver a mensagem de que a aplicação iniciou na porta 8080.*

3. **Iniciar a Backend:**
   Na pasta /backend, execute:
   ```bash
    ./mvnw spring-boot:run -DskipTests
    ```
    *A API estará disponível em http://localhost:8080.*

4. **Iniciar o Frontend:**
   Na pasta /frontend, execute:
   ```bash
    npm install
    npm run dev
    ```
    *Acesse a interface em http://localhost:3000*

5.  **Para parar a aplicação:**
    Na pasta raiz, exexute:
    ```bash
    docker-compose down
    ```
    Na pasta /backend:
    ```bash
    ctrl + c
    ```
    Na pasta /frontend:
    ```bash
    ctrl + c
    ```


## Endpoints da API

### 1. Registrar usuário
**POST** `/users/register`

Cria uma nova conta de usuário.

### 2. Logar usuário
**POST** `/users/login`

Autentica o usuário e retorna o Token JWT.

### 3. Encurtar uma URL (Privado - Requer Token JWT)
**POST** `/api/urls`

Envia uma URL longa e recebe o link encurtado associado à sua conta.

### 4. Acessar URL Encurtada
**GET** `/{codigo}`

Redireciona (HTTP 302) o visitante para o site original e contabiliza um clique.

Basta colar a URL encurtada no navegador.
* **Exemplo:** `http://localhost:8080/aX9z2`
* **Comportamento:** Redireciona (HTTP 302) para o site original.

### 5. Lista URLs do usuário (Paginado)
**GET** `/api/urls`

Apresenta as URLs que o usuário possui.

### 6. Deletar uma URL (Privado - Requer Token JWT)
**DELETE** `/api/urls/{id}`

Remove uma URL.

---
Desenvolvido com ☕ e Spring Boot.
