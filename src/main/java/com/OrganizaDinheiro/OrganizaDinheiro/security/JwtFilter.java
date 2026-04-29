package com.OrganizaDinheiro.OrganizaDinheiro.security;

import com.OrganizaDinheiro.OrganizaDinheiro.service.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

public class JwtFilter implements Filter {

    private final JwtService jwtService;

    // INJETA SERVIÇO PRA VALIDAR TOKEN
    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        jakarta.servlet.http.HttpServletResponse res = (jakarta.servlet.http.HttpServletResponse) response;

        //PEGA O HEADER AUTHORIZATION
        String header = req.getHeader("Authorization");

        // EXECUTA SE EXISTIR TOKEN VALIDO NO FORMATO DE BEARER
        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7);
                String phone = jwtService.validateToken(token);

                // DEFINE PERMISSOES DO USUSARIO (ROLE BASICA)
                var authorities = List.of(new SimpleGrantedAuthority("USER"));

                //CRIA UM OBJETO QUE REPRESENTA USUARIO AUTENTICADO
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(phone,null, authorities);

                //REGISTRA USUARIO NO CONTEXTO DO SPRING(ESSENCIAL)
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                res.setStatus(401); // RETORNA NAO AUTORIZADO
                res.getWriter().write("Token invalido ou expirado");
                return; //PARA EXECUCAO
            }
        }
        // CONTINUA O FLUXO DA REQUISICAO
        chain.doFilter(request, response);

    }
}

