package com.OrganizaDinheiro.OrganizaDinheiro.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SendCodeRequest {
    private String phone;
    private String code;
}
