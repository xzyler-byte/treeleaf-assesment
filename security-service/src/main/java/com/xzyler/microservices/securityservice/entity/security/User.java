package com.xzyler.microservices.securityservice.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xzyler.microservices.securityservice.enums.Gender;
import com.xzyler.microservices.securityservice.generic.api.AuditActiveAbstract;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * This Entity class maps with table Users in database.
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @for Security service
 * @since 1.0 - 2021
 */

@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends AuditActiveAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_SEQ_GEN")
    @SequenceGenerator(name = "User_SEQ_GEN", sequenceName = "User_SEQ", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(nullable = false, length = 70, unique = true)
    private String username;

    @JsonIgnore
    @Column(length = 256, nullable = false)
    private String password;

    @Column(nullable = false, length = 70, unique = true)
    private String email;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "full_name",nullable = false)
    private String fullName;

    @Column(length = 100)
    private String address;

    @Column(name = "password_reset_token",length = 100)
    private String passwordResetToken;

    @Column(name = "email_confirmed",nullable = false)
    private Boolean emailConfirmed;

    @Column(nullable = false)
    private Gender gender;

    @OneToMany(orphanRemoval = false, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_applicationUserImages"))
    @OrderBy("entryDate desc")
    private List<ApplicationUserImage> applicationUserImages;

    @OneToMany(orphanRemoval = false, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_userPasswordHists"))
    private List<UserPasswordHist> userPasswordHists;

}
