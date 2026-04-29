package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.service.JwtService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.OtpService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.SmsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final OtpService otpService;
    private final SmsService smsService;
    private final JwtService jwtService;

    @PostMapping("/send-code")
    public String senCode(@RequestBody Map<String, String> body){

        String phone = body.get("phone");
        String code = otpService.generateCode(phone);
        smsService.send(phone, "Seu codigo é: " + code);

        return "codigo enviado!";
    }

    @PostMapping
    public String validateCode(@RequestBody Map<String, String> body){
        String phone = body.get("phone");
        String code = body.get("code");

        if (otpService.validateCode(phone, code)){
            String token = jwtService.genetareToken(phone);
            return token;
        }
        return "Codigo invalido";
    }
}
