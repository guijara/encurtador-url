package com.guilherme.encurtador_url.user;

import com.guilherme.encurtador_url.config.TokenConfig;
import com.guilherme.encurtador_url.user.dto.AutenticationDto;
import com.guilherme.encurtador_url.user.dto.CreateUserDto;
import com.guilherme.encurtador_url.user.dto.LoginResponseDto;
import com.guilherme.encurtador_url.user.exception.UserExistsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenConfig tokenConfig;
    private AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenConfig tokenConfig, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenConfig = tokenConfig;
        this.authenticationManager = authenticationManager;
    }

//    public UserDetails loadUserByUsername(String username){
//
//        UserEntity userEntity = userRepository.findByUsername(username)
//                .orElseThrow(() -> new
//                        UsernameNotFoundException("Usuário não encontrado no sistema."));
//
//        return User.builder().username(userEntity.getUsername())
//                .password(userEntity.getPassword()).
//                roles(userEntity.getRole().name()).build();
//    }

    public LoginResponseDto verificaUsuarioParaLogin(AutenticationDto dto){

        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.username(),dto.passwod());
        var auth = authenticationManager.authenticate(usernamePassword);

        var user = userRepository.findByUsername(dto.username()).orElseThrow(() -> new UsernameNotFoundException("Usuario não existe no sistema"));

        var token = tokenConfig.generateToken(user);

        return new LoginResponseDto(token);
    }

    public void cadastrarUsuario(CreateUserDto dto) {

        Optional<UserEntity> user = userRepository.findByUsername(dto.username());

        if (user.isPresent()){
            throw new UserExistsException("O usuário "+dto.username()+" já existe no sistema.");
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(dto.username());

        userEntity.setPassword(passwordEncoder.encode(dto.password()));

        userEntity.setRole(Role.USER);

        userRepository.save(userEntity);
    }
}
