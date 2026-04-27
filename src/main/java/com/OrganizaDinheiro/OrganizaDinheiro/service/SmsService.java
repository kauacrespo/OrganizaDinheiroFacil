package com.OrganizaDinheiro.OrganizaDinheiro.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void send(String phone,String message){
        System.out.println("SMS para" + phone + ": " + message);
    }
}
