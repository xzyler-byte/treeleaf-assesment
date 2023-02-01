package com.xzyler.microservices.blogservice.entity;

import lombok.*;

import javax.persistence.*;


/**
 * This Entity class maps with table UserPasswordHistory in database.
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @for Security service
 * @since 1.0 - 2021
 */

@Entity
@Table(name = "Blog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Blog_SEQ_GEN")
    @SequenceGenerator(name = "Blog_SEQ_GEN", sequenceName = "Blog_SEQ", initialValue = 1, allocationSize = 1)
    private Integer id;

}
