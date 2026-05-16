package com.OrganizaDinheiro.OrganizaDinheiro.service;

import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public User registerUser(String name, String phone,String password){

        if (userRepository.existsByPhone(phone)){
            throw new RuntimeException("Usuario Ja cadastrado");
        }

        User user = new User();
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(password);

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

    public String validateLoginAndPassword(String phone, String password){

        if (userRepository.findByPhone(phone).isEmpty()){
          throw new RuntimeException("Usuario nao encontrado");
       }

        User user = userRepository.findByPhone(phone).get();

       if (password.equals(user.getPassword())){
           return jwtService.generateToken(phone);
       }else  {
           throw new RuntimeException("Senha incorreta");
       }
    }
}
