package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {
    private String phone;
    private String password;
}
