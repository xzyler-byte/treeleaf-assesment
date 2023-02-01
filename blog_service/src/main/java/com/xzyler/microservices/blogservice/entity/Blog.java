package com.xzyler.microservices.blogservice.entity;

import com.xzyler.microservices.blogservice.generic.api.AuditActiveAbstract;
import lombok.*;

import javax.persistence.*;
import java.util.List;


/**
 * This Entity class maps with table UserPasswordHistory in database.
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @for Security service
 * @since 1.0 - 2023
 */

@Entity
@Table(name = "Blog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Blog extends AuditActiveAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Blog_SEQ_GEN")
    @SequenceGenerator(name = "Blog_SEQ_GEN", sequenceName = "Blog_SEQ", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "text_content")
    private String textContent;

    @OneToMany(fetch = FetchType.LAZY)
    List<Comment> comments;


}
