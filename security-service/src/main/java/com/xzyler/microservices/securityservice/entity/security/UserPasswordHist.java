package com.xzyler.microservices.securityservice.entity.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


/**
 * This Entity class maps with table UserPasswordHistory in database.
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @since 1.0 - 2021
 * @for Security service
 */

@Entity
@Table(name = "User_Password_Hist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPasswordHist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserPasswordHist_SEQ_GEN")
    @SequenceGenerator(name = "UserPasswordHist_SEQ_GEN", sequenceName = "UserPasswordHist_SEQ", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_system_generated",nullable = false)
    private boolean isSystemGenerated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(name = "password_change_date")
    private Date passwordChangeDate;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName="id")
    private User userId;
}
