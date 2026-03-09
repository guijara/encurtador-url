package com.guilherme.encurtador_url.user;

import com.guilherme.encurtador_url.config.openapi.ApiStandardErrors;
import com.guilherme.encurtador_url.user.dto.AutenticationDto;
import com.guilherme.encurtador_url.user.dto.CreateUserDto;
import com.guilherme.encurtador_url.user.dto.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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


    @Operation(summary = "Registrar usuário", description = "Recebe username e " +"password e criar um usuário no banco caso não exista")
    @ApiStandardErrors
    @ApiResponse(responseCode = "201", description= "O processo deu certo e o usuário foi criado.")
    @ApiResponse(responseCode = "400", description= "O processo deu erro pois o usuário já existe no sistema.")
    @PostMapping("/register")
    public ResponseEntity<Void> cadastrarUsuario(@Valid @RequestBody CreateUserDto dto){

        userService.cadastrarUsuario(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "Logar usuário", description = "Recebe usuário e senha para realizar a autenticação do login")
    @ApiStandardErrors
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso e token recebido."),
            @ApiResponse(responseCode = "400", description = "O usuário enviado não existe no sistema."),
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> logarUsuario(@Valid @RequestBody AutenticationDto dto){

        LoginResponseDto dtoResponse= userService.verificaUsuarioParaLogin(dto);


        return ResponseEntity.ok(dtoResponse);
    }
}
