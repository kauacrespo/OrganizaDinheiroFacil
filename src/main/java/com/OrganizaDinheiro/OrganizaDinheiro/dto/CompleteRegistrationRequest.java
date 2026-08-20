package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompleteRegistrationRequest {
    private String phone;
    private String name;
}
