package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValidateCodeResponse {

    private boolean registred;

    private String message;

    private String token;
}
