package com.OrganizaDinheiro.OrganizaDinheiro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String phone;
    private String password;
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "userTransactions")
    List<Transaction> transactions;
}

