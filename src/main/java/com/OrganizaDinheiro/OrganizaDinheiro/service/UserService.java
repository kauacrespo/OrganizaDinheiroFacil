package com.OrganizaDinheiro.OrganizaDinheiro.service;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.LoginRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.dto.RegisterRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request){

        if (userRepository.existsByPhone(request.getPhone())){
            throw new RuntimeException("Usuario Ja cadastrado");
        }

        User user = new User();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        System.out.println("Usuario registrado!");
        return userRepository.save(user);
    }

    public boolean searchByPhone(String phone){
        if (userRepository.existsByPhone(phone)){
            System.out.println("telefone Ja exite no sistema");
            return userRepository.existsByPhone(phone);

        }else {
            throw new RuntimeException("telefone nao encontrado");
        }
    }

    public String validateLoginAndPassword(LoginRequest loginRequest){

        if (userRepository.findByPhone(loginRequest.getPhone()).isEmpty()){
          throw new RuntimeException("Usuario nao encontrado");
       }

        User user = userRepository.findByPhone(loginRequest.getPhone()).get();

       if (passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
           return jwtService.generateToken(loginRequest.getPhone());
       }else  {
           throw new RuntimeException("Senha incorreta");
       }
    }
}
