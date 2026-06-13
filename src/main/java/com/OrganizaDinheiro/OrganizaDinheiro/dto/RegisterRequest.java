package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String password;
    private String phone;
}
