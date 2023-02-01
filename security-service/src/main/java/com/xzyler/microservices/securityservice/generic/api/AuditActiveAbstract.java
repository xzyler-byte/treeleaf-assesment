package com.xzyler.microservices.securityservice.generic.api;

import com.xzyler.microservices.securityservice.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AuditActiveAbstract extends AuditAbstract {
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
