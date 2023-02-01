package com.xzyler.microservices.securityservice.entity.security.rolerightassignment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xzyler.microservices.securityservice.entity.security.Role;
import com.xzyler.microservices.securityservice.generic.api.AuditActiveAbstract;
import lombok.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * This Entity class maps with table RoleMenuRightAssignmentMaster in database.
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @for Security service
 * @since 1.0 - 2021
 */

@Entity
@Table(name = "Role_Menu_Right_Assignment_Master")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
public class RoleMenuRightAssignmentMaster extends AuditActiveAbstract {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RoleMenuRightAssignmentMaster_SEQ_GEN")
    @SequenceGenerator(name = "RoleMenuRightAssignmentMaster_SEQ_GEN", sequenceName = "RoleMenuRightAssignmentMaster_SEQ", initialValue = 1, allocationSize = 1)
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "effect_till_date")
    private Date effectTillDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "effect_date", nullable = false)
    private Date effectDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoleMenuRightAssignmentDetail> roleMenuRightAssignmentDetailList;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_roleMenuRightAssignmentMaster_role"), nullable = false)
    private Role role;
}
