package com.xzyler.microservices.securityservice.entity.security.rolerightassignment;

import com.xzyler.microservices.securityservice.entity.security.Menu;
import com.xzyler.microservices.securityservice.generic.api.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * This Entity class maps with table RoleMenuRightAssignmentDetail in database.
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @for Security service
 * @since 1.0 - 2021
 */

@Entity
@Table(name = "Role_Menu_Right_Assignment_Detail")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuRightAssignmentDetail implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RoleMenuRightAssignmentDetail_SEQ_GEN")
    @SequenceGenerator(name = "RoleMenuRightAssignmentDetail_SEQ_GEN", sequenceName = "RoleMenuRightAssignmentDetail_SEQ", initialValue = 1, allocationSize = 1)
    @Column( nullable = false)
    private Integer id;

    @Column(nullable = false)
    private boolean create;

    @Column(nullable = false)
    private boolean view;

    @Column(nullable = false)
    private boolean update;

    @Column(nullable = false)
    private boolean delete;

    @Column( nullable = false)
    private boolean approve;

    @Column(nullable = false)
    private boolean report;

    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_roleMenuRight_menu"), nullable = false)
    private Menu menu;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_menu_right_assignment_master_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_roleMenuRight_masterId"))
    private RoleMenuRightAssignmentMaster roleMenuRightAssignmentMaster;

}
