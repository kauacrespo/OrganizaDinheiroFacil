package com.OrganizaDinheiro.OrganizaDinheiro.service;

import com.OrganizaDinheiro.OrganizaDinheiro.model.CodeOtp;
import com.OrganizaDinheiro.OrganizaDinheiro.repositoy.CodeOtpRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final CodeOtpRepository repository;

    public OtpService(CodeOtpRepository repository) {
        this.repository = repository;
    }

    //GERA UM NUMERO DE 6 DIGITOS ALEATORIO
    public String generateCode(String phone){
        String code = String.valueOf(100000 + new Random().nextInt(900000));

        //DEFINE UM TEMPO DE EXPIRAÇAO DE 5 MINUTOS
        CodeOtp codeOtp = CodeOtp.builder()
                .phone(phone)
                .code(code)
                .dateTime(LocalDateTime.now().plusMinutes(5))
                .build();

     //SALVA NO BANCO DE DADOS O NUMERO
        repository.save(codeOtp);

        return code;
    }

    //PEGA O ULTIMO CODIGO DO TELEFONE
    public boolean validateCode(String phone, String code){

        //COMPARA O CODIGO DIGITADO E VERIFICA SE NAO EXPIROU
        return repository.findTopByTelefoneOrderByExpiraEmDesc(phone)
                .filter(o -> o.getCode().equals(code))
                .filter(o -> o.getDateTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
