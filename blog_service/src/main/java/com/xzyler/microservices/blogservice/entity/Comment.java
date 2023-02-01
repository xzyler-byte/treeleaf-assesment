package com.xzyler.microservices.blogservice.entity;

import com.xzyler.microservices.blogservice.generic.api.AuditActiveAbstract;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends AuditActiveAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Comment_SEQ_GEN")
    @SequenceGenerator(name = "Comment_SEQ_GEN", sequenceName = "Comment_SEQ", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "is_anonymous_user")
    private Boolean isAnonymousUser;

    @Column(name = "message")
    private String message;

    @Column(name = "user_id")
    private Integer user_id;

    //Parent Child concept i.e. this is comment id
    @Column(name = "reply_id")
    private Integer reply;
}
