package com.xzyler.microservices.securityservice.repository.security;

import com.xzyler.microservices.securityservice.entity.security.UserPasswordHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordHistRepository extends JpaRepository<UserPasswordHist, Integer> {
}
