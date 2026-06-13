package com.OrganizaDinheiro.OrganizaDinheiro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
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
    List<Expense> Expenses;
}

