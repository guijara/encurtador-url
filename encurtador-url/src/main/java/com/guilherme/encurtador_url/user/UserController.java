package com.guilherme.encurtador_url.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Endpoints para login e registro de usuários")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @Operation(summary = "Registra usuário", description = "Recebe username e " +
            "password e criar um usuário no banco caso não exista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201 Created", description
                    = "O processo deu certo e o usuário foi criado."),
            @ApiResponse(responseCode = "400 Bad Request", description
                    = "O processo deu erro pois o usuário já existe no sistema."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/register")
    public void cadastrarUsuario(@Valid @RequestBody CreateUserDto dto){

        userService.cadastrarUsuario(dto);

        ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public void logarUsuario(@Valid @RequestBody UserLoginDto dto){
        
    }
}
