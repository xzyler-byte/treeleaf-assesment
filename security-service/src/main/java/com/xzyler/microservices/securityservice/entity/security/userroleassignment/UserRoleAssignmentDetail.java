package com.xzyler.microservices.securityservice.entity.security.userroleassignment;

import com.xzyler.microservices.securityservice.entity.security.Role;
import com.xzyler.microservices.securityservice.generic.api.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "User_Role_Assignment_Detail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleAssignmentDetail implements BaseEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserRoleAssignmentDetail_SEQ_GEN")
    @SequenceGenerator(name = "UserRoleAssignmentDetail_SEQ_GEN", sequenceName = "UserRoleAssignmentDetail_SEQ_GEN", initialValue = 1, allocationSize = 1)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_UserRoleAssignmentDetail_RoleId"))
    private Role role;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role_assignment_master_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_UserRoleAssignmentDetail_MasterId"))
    private UserRoleAssignmentMaster userRoleAssignmentMaster;
}
