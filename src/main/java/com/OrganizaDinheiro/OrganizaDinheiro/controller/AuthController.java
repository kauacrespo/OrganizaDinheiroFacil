package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.service.OtpService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.SmsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final OtpService otpService;
    private final SmsService smsService;

    public AuthController(OtpService otpService, SmsService smsService) {
        this.otpService = otpService;
        this.smsService = smsService;
    }

    @PostMapping("/sen-code")
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
            return "Login OK (gerar JWT aqui)";
        }
        return "Codigo invalido";
    }
}
