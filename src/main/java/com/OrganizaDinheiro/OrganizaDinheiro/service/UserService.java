package com.OrganizaDinheiro.OrganizaDinheiro.service;

import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User createUser(String phone, String name) {
        if (userRepository.existsByPhone(phone)) {
            throw new RuntimeException("Usuario ja cadastrado!");
        }
        User user = new User();
        user.setPhone(phone);
        user.setName(name);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }


    public User findUserByPhone(String phone) {

        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado!"));
    }
}
