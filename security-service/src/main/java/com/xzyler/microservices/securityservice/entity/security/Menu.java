package com.xzyler.microservices.securityservice.entity.security;

import com.xzyler.microservices.securityservice.generic.api.AuditAbstract;
import lombok.*;

import javax.persistence.*;

/**
 * This Entity class maps with table Menu in database.
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @for Security service
 * @since 1.0 - 2021
 */
@Entity
@Table(name = "Menu",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"Menu_Name"}, name = "UNIQUE_MENU_MENUNAME"),
                @UniqueConstraint(columnNames = {"Menu_Code"}, name = "UNIQUE_MENU_MENUCODE"),

        })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends AuditAbstract {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Menu_SEQ_GEN")
    @SequenceGenerator(name = "Menu_SEQ_GEN", sequenceName = "Menu_SEQ", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "menu_name", unique = true, length = 150, nullable = false)
    private String menuName;

    @Column(name = "order_id", nullable = false)
    private Integer order;

    @Column(name = "menu_code", unique = true, length = 20)
    private String menuCode;

    @Column(name = "icon_class", length = 20)
    private String iconClass;

    @Column(name = "master_menu_id")
    private Integer masterMenuId;

    @Column(name = "url", length = 150)
    private String url;

    @Column(name = "is_hidden", nullable = false)
    private Boolean isHidden;
}
