package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/User")
public class UserController {


    @GetMapping()
    public String UserColectInfo(@RequestBody User user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String telefone = user.getName();
        return HttpServletResponse.SC_OK+"\n"+telefone;
    }
}
