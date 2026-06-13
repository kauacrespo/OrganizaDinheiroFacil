package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/User")
public class UserController {


    @GetMapping
    public ResponseEntity<LoginRequest> userLogin(){

        return ResponseEntity.status(200).body(new LoginRequest());
    }
}
