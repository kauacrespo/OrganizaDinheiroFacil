package com.OrganizaDinheiro.OrganizaDinheiro.settings;

import com.OrganizaDinheiro.OrganizaDinheiro.security.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    // INJETA O FILTRO (ESSENCIAL)
     private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // DESABILITA PROTEÇAO DE FORMULARIO  (NÃO USAMOS API)
        http.csrf((csrf) -> csrf.disable())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated());

        // ADICIONA MEU FILTRO NA CADEIA
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // RETORNA CONFIGURAÇAO FINAL (OBRIGATÓRIO)
    return http.build();

    }
}
