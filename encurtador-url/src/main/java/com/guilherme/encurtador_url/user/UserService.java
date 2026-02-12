package com.guilherme.encurtador_url.user;

import com.guilherme.encurtador_url.user.exception.UserExistsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username){

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new
                        UsernameNotFoundException("Usuário não encontrado no sistema."));

        return User.builder().username(userEntity.getUsername())
                .password(userEntity.getPassword()).roles(userEntity.getRole()).build();
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
