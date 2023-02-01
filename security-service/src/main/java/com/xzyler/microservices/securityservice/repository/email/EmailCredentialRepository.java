package com.xzyler.microservices.securityservice.repository.email;

import com.xzyler.microservices.securityservice.entity.security.email.EmailCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCredentialRepository extends JpaRepository<EmailCredentials, Integer> {

    @Query(value = "select * from Email_Credentials where status ORDER BY date desc limit 1", nativeQuery = true)
    EmailCredentials findOneByActive();
}
