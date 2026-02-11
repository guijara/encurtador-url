package com.guilherme.encurtador_url.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private (String username){
        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isPresent()){

        }
    }
}
