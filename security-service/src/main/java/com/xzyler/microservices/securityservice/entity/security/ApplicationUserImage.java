package com.xzyler.microservices.securityservice.entity.security;

import com.xzyler.microservices.securityservice.generic.api.BaseEntity;
import com.xzyler.microservices.securityservice.util.MultipartFileHandler;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

/**
 * This Entity class maps with table UserImages in database.
 *
 * @author Nitesh Thapa
 * @version 1.0
 * @for Security service
 * @since 1.0 - 2021
 */

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Application_User_Image")
public class ApplicationUserImage implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Application_User_Image_SEQ_GEN")
    @SequenceGenerator(name = "Application_User_Image_SEQ_GEN", sequenceName = "Application_User_Image_SEQ", initialValue = 1, allocationSize = 1)
    @Column(nullable = false)
    private Integer id;

    @Column(columnDefinition = "text")
    private String image;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User userId;

    public void setImage(MultipartFile image) throws Exception {
        this.image = new MultipartFileHandler().handle(image);
    }
}
