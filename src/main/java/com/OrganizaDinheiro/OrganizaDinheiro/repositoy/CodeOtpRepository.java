package com.OrganizaDinheiro.OrganizaDinheiro.repositoy;

import com.OrganizaDinheiro.OrganizaDinheiro.model.CodeOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeOtpRepository extends JpaRepository<CodeOtp, Long> {

    Optional<CodeOtp> findTopByTelefoneOrderByExpiraEmDesc(String phone);
}
