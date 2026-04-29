package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
public class UserController {


    @GetMapping()
    public String UserColectInfo(@RequestBody User user){

    }
}
