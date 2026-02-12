package com.guilherme.encurtador_url.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username){

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new
                        UsernameNotFoundException("Usuário não encontrado no sistema."));

        return User.builder().username(userEntity.getUsername())
                .password(userEntity.getPassword()).roles(userEntity.getRole()).build();
    }
}
