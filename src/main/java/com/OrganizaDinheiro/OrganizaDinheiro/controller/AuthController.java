package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.LoginRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.dto.RegisterRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.dto.SendCodeRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.service.JwtService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.OtpService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.SmsService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final OtpService otpService;
    private final SmsService smsService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody SendCodeRequest sendCodeRequest) {

        String phone = sendCodeRequest.getPhone();
        String code = otpService.generateCode(phone);
        smsService.send(phone, "Seu codigo é: " + code);

        return ResponseEntity.status(HttpStatus.OK).body("Código enviado com sucesso");
    }

    @PostMapping("/validate-code")
    public ResponseEntity<String> validateCode(@RequestBody SendCodeRequest sendCodeRequest) {
        String phone = sendCodeRequest.getPhone();
        String code = sendCodeRequest.getCode();

        if (otpService.validateCode(phone, code)) {
            String token = jwtService.generateToken(phone);
            return ResponseEntity.status(200).body(token);
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest){
        userService.registerUser(registerRequest);
        return ResponseEntity.status(201).body("Registro realizado com sucesso");
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest){
        String token = userService.validateLoginAndPassword(loginRequest);
        return ResponseEntity.status(200).body(token);
    }

}
