package com.xzyler.microservices.securityservice.entity.security.userroleassignment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xzyler.microservices.securityservice.generic.api.AuditActiveAbstract;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "User_Role_Assignment_Master")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleAssignmentMaster extends AuditActiveAbstract {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserRoleAssignmentMaster_SEQ_GEN")
    @SequenceGenerator(name = "UserRoleAssignmentMaster_SEQ_GEN", sequenceName = "UserRoleAssignmentMaster_SEQ", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Basic(optional = true)
    @Column(name = "effect_till_date")
    private Date effectTillDate;

    @Column(name = "effect_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    private Date effectDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_assignment_master_id")
    private List<UserRoleAssignmentDetail> userRoleAssignmentDetailList;
}
