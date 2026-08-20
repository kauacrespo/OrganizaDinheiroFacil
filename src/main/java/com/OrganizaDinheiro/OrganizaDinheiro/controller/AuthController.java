package com.OrganizaDinheiro.OrganizaDinheiro.controller;

import com.OrganizaDinheiro.OrganizaDinheiro.dto.CompleteRegistrationRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.dto.SendCodeRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.dto.ValidateCodeRequest;
import com.OrganizaDinheiro.OrganizaDinheiro.dto.ValidateCodeResponse;
import com.OrganizaDinheiro.OrganizaDinheiro.model.User;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.UserRepository;
import com.OrganizaDinheiro.OrganizaDinheiro.service.JwtService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.OtpService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.SmsService;
import com.OrganizaDinheiro.OrganizaDinheiro.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final OtpService otpService;
    private final SmsService smsService;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody SendCodeRequest sendCodeRequest) {

        String phone = sendCodeRequest.getPhone();
        String code = otpService.generateCode(phone);
        smsService.send(phone, "Seu codigo é: " + code);

        return ResponseEntity.status(HttpStatus.OK).body("Código enviado com sucesso");
    }

    @PostMapping("/validate-code")
    public ResponseEntity<ValidateCodeResponse> validateCode(@RequestBody ValidateCodeRequest  validateCodeRequest) {

        String phone = validateCodeRequest.getPhone();
        String code = validateCodeRequest.getCode();

        boolean valid = otpService.validateCode(phone, code);

        if (!valid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ValidateCodeResponse(
                            false,
                            "Código inválido ou expirado",
                            null
                    ));
        }

        Optional<User> optionalUser =
                userRepository.findByPhone(phone);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            String token = jwtService.generateToken(user.getId());

            return ResponseEntity.ok(
                    new ValidateCodeResponse(
                            true,
                            "Login realizado com sucesso",
                            token
                    )
            );
        }
        return ResponseEntity.ok(
                new ValidateCodeResponse(
                        false,
                        "Telefone confirmado Informe seu nome.",
                        null
                )
        );
    }
    @PostMapping("/complete-registration")
    public ResponseEntity<String> completeRegistration(
            @RequestBody CompleteRegistrationRequest request) {

        User user = userService.createUser(
                request.getPhone(),
                request.getName()
        );

        String token =
                jwtService.generateToken(user.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(token);

    }
}
