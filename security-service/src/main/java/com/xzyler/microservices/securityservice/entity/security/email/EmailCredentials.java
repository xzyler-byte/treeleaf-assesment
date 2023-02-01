package com.xzyler.microservices.securityservice.entity.security.email;

import com.xzyler.microservices.securityservice.generic.api.AuditActiveAbstract;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "Email_Credentials")
public class EmailCredentials extends AuditActiveAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Email_Seq_GEN")
    @SequenceGenerator(name = "Email_Seq_GEN", sequenceName = "Email_Seq", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @Column
    private String host;
    @Column
    private String port;
    @Column
    private Date date;
    @Column
    private String protocol;
}
