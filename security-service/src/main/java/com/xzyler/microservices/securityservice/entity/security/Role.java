package com.xzyler.microservices.securityservice.entity.security;

import com.xzyler.microservices.securityservice.generic.api.AuditAbstract;
import com.xzyler.microservices.securityservice.generic.api.AuditActiveAbstract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonAutoDetect;

import javax.persistence.*;

/**
 * This Entity class maps with table UserRole in database.
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @for Security service
 * @since 1.0 - 2021
 */
@Entity
@Table(name = "User_Role", uniqueConstraints = {
        @UniqueConstraint(name = "UNIQUE_ROLE_ROLENAME_EN", columnNames = "role_name")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonAutoDetect
public class Role extends AuditActiveAbstract {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Role_SEQ_GEN")
    @SequenceGenerator(name = "Role_SEQ_GEN", sequenceName = "Role_SEQ", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "role_name",nullable = false, length = 40, unique = true)
    private String roleName;
}
