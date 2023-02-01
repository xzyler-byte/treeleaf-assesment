package com.xzyler.microservices.blogservice.dto.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Mail<T> implements Serializable {

    private String to;
    private String subject;
    private String template;
    private List<Object> attachments;
    private List<MultipartFile> files;
    private List<String> filePaths;
    private String[] toList;
    private T model;

}
