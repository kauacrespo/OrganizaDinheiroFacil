package com.OrganizaDinheiro.OrganizaDinheiro.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "sua-chave-com-32-caracteres-no-minimo";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    //CRIA O METODO DE GERAR O TOKEN ALEATORIO COM BASE NO NUMERO DE TELEFONE INFORMADO
    public String generateToken(String phone){
        return Jwts.builder()
                .setSubject(phone)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // dia
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    //VALIDA O TOKEN RECEBIDO
    public String validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token invalido ou expirado");
        }
    }
}
