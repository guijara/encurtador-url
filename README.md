# Encurtador de URL

Uma API robusta e escalável para encurtamento de URLs, desenvolvida com Java, Spring Boot e Docker. Este projeto permite transformar URLs longas em links curtos, contando com um sistema completo de autenticação JWT para gerenciar as URLs dos usuários.

## Tecnologias Utilizadas

* **Java 21** - Linguagem base.
* **Spring Boot 4** - Framework para criação da API.
* **Spring Security & JWT** - Autenticação e autorização via tokens.
* **PostgreSQL** - Banco de dados relacional robusto.
* **Docker & Docker Compose** - Para containerização e orquestração do ambiente.
* **Flyway** - Para migração e versionamento do banco de dados.
* **Swagger (OpenAPI)** - Documentação interativa da API.

## ⚙️ Pré-requisitos

Para rodar este projeto, precisas apenas ter instalado:
* [Docker](https://www.docker.com/) e Docker Compose.
* (Opcional) Java 21 e Maven se quiseres rodar fora do Docker.

## 🛠️ Como Rodar (Passo a Passo)

A forma mais simples é utilizando o Docker, que configura automaticamente o Banco de Dados e a Aplicação.

1.  **Clone o repositório** (ou baixe os arquivos):
    ```bash
    git clone <seu-link-git-aqui>
    ```

2.  **Inicie o ambiente:**
    Na raiz do projeto, execute:
    ```bash
    docker-compose up --build
    ```
    *Aguarde até ver a mensagem de que a aplicação iniciou na porta 8080.*

3.  **Para parar o ambiente:**
    ```bash
    docker-compose down
    ```

## Endpoints da API

### 1. Registrar usuário
**POST** `/users/register`

Cria uma nova conta de usuário.

* **Exemplo de Corpo (JSON):**
    ```json
    {
      "username": "lordeJason",
      "password": "fu23f02f"
    }
    ```

### 2. Logar usuário
**POST** `/users/login`

Autentica o usuário e retorna o Token JWT.

* **Exemplo de Corpo (JSON):**
    ```json
    {
      "username": "lordeJason",
      "password": "fu23f02f"
    }
    ```

### 3. Encurtar uma URL (Privado - Requer Token JWT)
**POST** `/api/urls`

Envia uma URL longa e recebe o link encurtado associado à sua conta.

* **Exemplo de Corpo (JSON):**
    ```json
    {
      "url": "https://www.google.com"
    }
    ```

### 4. Acessar URL Encurtada
**GET** `/{codigo}`

Redireciona (HTTP 302) o visitante para o site original e contabiliza um clique.

Basta colar a URL encurtada no navegador.
* **Exemplo:** `http://localhost:8080/aX9z2`
* **Comportamento:** Redireciona (HTTP 302) para o site original.

---
Desenvolvido com ☕ e Spring Boot.
